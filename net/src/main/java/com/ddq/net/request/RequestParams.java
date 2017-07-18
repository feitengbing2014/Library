package com.ddq.net.request;

import com.ddq.net.response.parser.BaseParser;
import com.ddq.net.response.parser.JsonParser;
import com.ddq.net.response.parser.Parser;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
//@formatter:off
/**
 * Created by dongdaqing on 2017/7/3.
 * 发起请求需要的参数，如果通过构造函数来构造对象就要么必须定义多个构造函数，要么构造对象的代码非常难看，
 * 为了避免麻烦要构造RequestParam必须通过Builder类，

 * {@link RequestParams}的泛型T是请求的返回的具体类型，解析器要正常工作就必须取到具体的类型，
 * 即运行时代码传递进来的真实类型，通过实验得出，要获取真实的泛型类型，用来获取泛型的类必须有包含具体类型的类代码。
 * eg：下面以本类为例
 *   我们要获取{@link RequestParams}的泛型，即运行时传进来的T
 *   如果直接构造{@link RequestParams}：假设有一个User类
 *      <code>
 *          RequestParams&lt;User&gt; rp = new RequestParams&lt;User&gt;();
 *      </code>
 *      上述代码构造的对象rp，我们通过rp是无论如何也无法获取到类型User的。
 *      为什么？
 *      具体获取泛型的操作是获取对象的类的Class对象，然后从Class对象中获取泛型信息，rp
 *      对象的类是RequestParams&lt;T&gt;而不是RequestParams&lt;User&gt;，所以通过rp对象获取到的类型都是 T 而不是 User，
 *      换一个方向来想，假设真的能够通过上述对象rp获取到User:
 *      <code>
 *          RequestParams&lt;A&gt; rpa = new RequestParams&lt;A&gt;();
 *          RequestParams&lt;B&gt; rpb = new RequestParams&lt;B&gt;();
 *          RequestParams&lt;C&gt; rpc = new RequestParams&lt;C&gt;();
 *      </code>
 *      由于我们是通过类来获取泛型而不是通过成员变量来获取的，所以无论怎么构造对象都不会对类本身的信息有任何的更改，
 *      因为类本身的信息在编译时就已经固定了。试想如果之前的假设成立，那么就是说能够在运行时动态修改class本身的信息
 *      那么上述代码中通过rpa对象的类获取到的泛型到底是A、B还是C？这样就很混乱了。
 *      再回到之前，下面的做法是可以获取泛型的
 *      <code>
 *          public class AAA extends RequestParams&lt;User&gt;{
 *
 *          }
 *
 *          AAA aa = new AAA();
 *      </code>
 *      上述对象aa是可以获取RequestParams泛型类型的真实类型的，因为aa的所在的类AAA直接继承自RequestParams&lt;User&gt;，泛型类型已经固定为User
 *
 *      所以想动态获取RequestParams的泛型是不可能的
 *
 *      既然不能动态获取那么就只能通过继承的方式来做了
 *      于是就有了
 *      <code>
 *          RequestParams&lt;User&gt; aa = new RequestParams&lt;User&gt;(){};
 *      </code>
 *      由于匿名内部类在编译时会生成具体的代码，所以上述代码就如同上面的显式继承了
 *      所以通过aa能够获取到泛型User，但这种做法不是动态获取
 *
 *      可能有人会觉得后面的一对大括号看着不爽，想要把它封装起来，经过实践得出这是无法做到的，
 *      因为要把它封装起来就相当于是传递泛型：
 *      <code>
 *           //假设{@link RequestParams}有一个getType()函数
 *           public Type getType(){
 *               BeanParser&lt;T&gt; parser = new BeanParser&lt;T&gt;(){};
 *               ----通过parser获取泛型---
 *           }
 *
 *           RequestParams&lt;A&gt; rpa = new RequestParams&lt;A&gt;();
 *           rpa.getType();
 *      </code>
 *      上述代码获取到的泛型还是T而不是A，因为这么做泛型是是传递不进去的，
 *      真是的流程其实就相当于构造了一个BeanParser的匿名子类，这个匿名子类的泛型仍旧是T，
 *      然后又new了一个这个匿名子类的对象，new这个匿名子类对象时传递进去了泛型A，这其实就是最上面的类型
 *
 *      通过上面的描述可以看出只要构造一个匿名子类对象即可获取到真正的泛型，那么为什么Builder是抽象的？
 *      这么做可以防止在写代码的时候忘了加{}而导致解析出错。
 */
//@formatter:on
public class RequestParams<T> implements Params<T> {
    private int tag;
    private String url;
    private HttpMethod mMethod;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private Parser<T> mParser;//响应解析器

    protected RequestParams() {
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Map<String, String> params() {
        return mParams;
    }

    @Override
    public Map<String, String> headers() {
        return mHeaders;
    }

    @Override
    public HttpMethod method() {
        return mMethod;
    }

    @Override
    public Parser<T> parser() {
        return mParser;
    }

    @Override
    public int tag() {
        return tag;
    }

    public void param(String key, String value) {
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        if (value != null)
            mParams.put(key, value);
    }

    public void setParams(Map<String, String> map) {
        if (mParams == null)
            mParams = map;
        else
            mParams.putAll(map);
    }

    public static class Builder<T> {
        private RequestParams<T> mParams;
        private Type mType;

        public Builder() {
            mParams = new RequestParams<>();
        }

        public Builder(RequestParams<T> params) {
            mParams = params;
        }

        public Builder<T> url(String url) {
            mParams.url = url;
            return this;
        }

        /**
         * 和下面的方法可以二选一
         *
         * @param key
         * @return
         */
        public Builder<T> key(String key) {
            if (mParams.mParser == null) {
                JsonParser<T> parser = new JsonParser<>();
                parser.setKey(key);
                mParams.mParser = parser;
            } else if (mParams.mParser instanceof JsonParser) {
                JsonParser<T> parser = (JsonParser<T>) mParams.mParser;
                parser.setKey(key);
            }
            return this;
        }

        /**
         * 具体意义见{@link JsonParser}
         *
         * @param cls
         * @return
         */
        public Builder<T> cls(Class cls) {
            if (mParams.mParser == null) {
                JsonParser<T> parser = new JsonParser<>();
                parser.setCls(cls);
                mParams.mParser = parser;
            } else if (mParams.mParser instanceof JsonParser) {
                JsonParser<T> parser = (JsonParser<T>) mParams.mParser;
                parser.setCls(cls);
            }
            return this;
        }

        public Builder<T> parser(Parser<T> parser) {
            mParams.mParser = parser;
            return this;
        }

        public Builder<T> tag(int tag) {
            mParams.tag = tag;
            return this;
        }

        public Builder<T> method(HttpMethod method) {
            mParams.mMethod = method;
            return this;
        }

        public Builder<T> param(String key, String value) {
            mParams.param(key, value);
            return this;
        }

        public Builder<T> params(Map<String, String> params) {
            mParams.setParams(params);
            return this;
        }

        public Builder<T> header(String key, String value) {
            if (mParams.mHeaders == null) {
                mParams.mHeaders = new HashMap<>();
            }
            if (value != null)
                mParams.mHeaders.put(key, value);
            return this;
        }

        /**
         * 如果需要封装RequestParams，请通过这个函数传入具体类型，这个函数
         *
         * @param type
         */
        public Builder<T> setType(Type type) {
            mType = type;
            //表示已经设置了type，不用再去自动获取了
            if (mType != null)
                param(BaseParser.TYPE_SET, "1");
            else
                mParams.mParams.remove(JsonParser.TYPE_SET);
            return this;
        }

        public RequestParams<T> get() {
            if (mParams.mParser == null) {
                mParams.mParser = new JsonParser<>();
            }

            mParams.mParser.setType(mType);

            if (mParams.mMethod == null)
                mParams.mMethod = HttpMethod.GET;
            return mParams;
        }
    }
}
