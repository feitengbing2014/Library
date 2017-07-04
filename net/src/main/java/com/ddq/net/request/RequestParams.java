package com.ddq.net.request;

import com.ddq.net.response.parser.BeanParser;
import com.ddq.net.response.parser.Parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongdaqing on 2017/7/3.
 * 发起请求需要的参数
 */

public class RequestParams<T> implements Params<T> {
    private int tag;
    private String url;
    private HttpMethod mMethod;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private Parser<T> mParser;//响应解析器

    private RequestParams() {
    }

    private RequestParams(int tag, String url, HttpMethod method, Map<String, String> params, Parser<T> parser) {
        this.tag = tag;
        this.url = url;
        mMethod = method;
        mParams = params;
        mParser = parser;
    }

    public static <T> RequestParams<T> GET(int tag, String url, Map<String, String> params) {
        return new RequestParams<>(tag, url, HttpMethod.GET, params, new BeanParser<T>() {
        });
    }

    public static <T> RequestParams<T> GET(int tag, String url, Map<String, String> params, String key) {
        return new RequestParams<>(tag, url, HttpMethod.GET, params, new BeanParser<T>(key) {
        });
    }

    public static <T> RequestParams<T> GET(int tag, String url, Map<String, String> params, Class cls) {
        return new RequestParams<>(tag, url, HttpMethod.GET, params, new BeanParser<T>(cls) {
        });
    }

    public static <T> RequestParams<T> POST(int tag, String url, Map<String, String> params) {
        return new RequestParams<>(tag, url, HttpMethod.POST, params, new BeanParser<T>() {
        });
    }

    public static <T> RequestParams<T> POST(int tag, String url, Map<String, String> params, String key) {
        return new RequestParams<>(tag, url, HttpMethod.POST, params, new BeanParser<T>(key) {
        });
    }

    public static <T> RequestParams<T> POST(int tag, String url, Map<String, String> params, Class cls) {
        return new RequestParams<>(tag, url, HttpMethod.POST, params, new BeanParser<T>(cls) {
        });
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
        return null;
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

    public static class Builder<T> {
        private RequestParams<T> mParams;

        public Builder() {
            mParams = new RequestParams<>();
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
            mParams.mParser = new BeanParser<T>(key) {
            };
            return this;
        }

        /**
         * 具体意义见{@link BeanParser}
         *
         * @param cls
         * @return
         */
        public Builder<T> cls(Class cls) {
            mParams.mParser = new BeanParser<T>(cls) {
            };
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
            if (mParams.mParams == null) {
                mParams.mParams = new HashMap<>();
            }
            if (value != null)
                mParams.mParams.put(key, value);
            return this;
        }

        public Builder<T> params(Map<String, String> params) {
            mParams.mParams = params;
            return this;
        }

        public Builder<T> header(String key, String value){
            if (mParams.mHeaders == null) {
                mParams.mHeaders = new HashMap<>();
            }
            if (value != null)
                mParams.mHeaders.put(key, value);
            return this;
        }

        public RequestParams<T> get() {
            if (mParams.mParser == null)
                mParams.mParser = new BeanParser<T>() {
                };
            return mParams;
        }
    }
}
