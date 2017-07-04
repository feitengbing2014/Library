package com.ddq.net.response.parser;

import com.ddq.net.error.BusinessError;
import com.ddq.net.exception.ParseException;
import com.ddq.net.response.annotation.Code;
import com.ddq.net.response.annotation.Data;
import com.ddq.net.response.annotation.Message;
import com.ddq.net.util.IOUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongdaqing on 2017/7/3.
 * 解析器，将服务端返回的数据解析成类型T，保持为抽象类是为了获取泛型
 */

public abstract class BeanParser<T> implements Parser<T> {

    /**
     * 下面两个变量如果key非空，优先使用key解析，解析失败
     * 则选用cls进行解析，cls一定要使用{@link com.ddq.net.response.annotation.Code}、
     * {@link com.ddq.net.response.annotation.Data}、{@link com.ddq.net.response.annotation.Message}
     * 三个注解对相应字段进行标注，不然即使传进来也没有意义
     */
    private Class<?> cls;
    private String key;
    private static final Map<Class, AnnotationBundle> cache = new HashMap<>();

    public BeanParser() {
    }

    public BeanParser(String key) {
        this.key = key;
    }

    public BeanParser(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public T parse(InputStream is, int length) throws ParseException {
        String response = IOUtil.readString(is);
        if (response == null) {
            throw new ParseException("failed to read response");
        }

        if (response.length() != length) {
            throw new ParseException("broken response");
        }

        String value2parse = response;
        if (key != null) {
            try {
                JSONObject object = new JSONObject(response);
                value2parse = object.getString(key);
            } catch (JSONException e) {
                throw new ParseException("failed to get key:" + key + " from " + response);
            }
        } else if (cls != null) {
            AnnotationBundle ab = cache.get(cls);
            if (ab == null) {
                ab = new AnnotationBundle();
                ab.extractAnnotation(cls);
                cache.put(cls, ab);
            }

            try {
                JSONObject object = new JSONObject(response);
                int code = ab.getCode(object);
                //请求成功
                if (ab.isSuccess(code)) {
                    value2parse = ab.getData(object);
                } else {
                    //请求失败，抛出业务出错
                    String message = ab.getMessage(object);
                    throw new ParseException(new BusinessError(code, message, response));
                }
            } catch (JSONException e) {
                throw new ParseException(e.getMessage());
            }
        }

        return new Gson().fromJson(value2parse, TokenHelper.getType(this.getClass()));
    }

    private static class AnnotationBundle {
        private String code;
        private int successCode = Integer.MIN_VALUE;
        private String[] message;
        private String[] data;

        AnnotationBundle() {
            message = new String[0];
            data = new String[0];
        }

        private String[] add(String[] dst, String value) {
            String[] tmp = new String[dst.length + 1];
            System.arraycopy(dst, 0, tmp, 0, dst.length);
            tmp[dst.length] = value;
            return tmp;
        }

        void extractAnnotation(Class cls) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                Code code = field.getAnnotation(Code.class);
                if (code != null) {
                    this.code = field.getName();
                    this.successCode = code.code();
                    continue;
                }

                if (field.getAnnotation(Message.class) != null) {
                    message = add(message, field.getName());
                    continue;
                }

                if (field.getAnnotation(Data.class) != null) {
                    data = add(data, field.getName());
                }
            }
        }

        int getCode(JSONObject o) throws JSONException {
            return o.getInt(code);
        }

        String getData(JSONObject o) throws JSONException {
            return getNotNull(o, data);
        }

        String getMessage(JSONObject o) throws JSONException {
            return getNotNull(o, message);
        }

        private String getNotNull(JSONObject o, String[] ss) throws JSONException {
            for (int i = 0; i < ss.length; i++) {
                String d = o.getString(ss[i]);
                if (d != null)
                    return d;
            }
            return null;
        }

        boolean isSuccess(int rp) {
            return rp == successCode;
        }
    }
}
