package com.ddq.net.response.parser;

import com.ddq.net.error.BusError;
import com.ddq.net.exception.ParseException;
import com.ddq.net.response.annotation.Code;
import com.ddq.net.response.annotation.Data;
import com.ddq.net.response.annotation.Message;
import com.ddq.net.util.IOUtil;
import com.ddq.net.util.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dongdaqing on 2017/7/3.
 * json格式数据解析器
 */

public final class JsonParser<T> extends BaseParser<T> {

    /**
     * 下面两个变量如果key非空，优先使用key解析，解析失败
     * 则选用cls进行解析，cls一定要使用{@link com.ddq.net.response.annotation.Code}、
     * {@link com.ddq.net.response.annotation.Data}、{@link com.ddq.net.response.annotation.Message}
     * 三个注解对相应字段进行标注，不然即使传进来也没有意义
     */
    private Class<?> cls;
    private String key;
    private static final Map<Class, AnnotationBundle> cache = new HashMap<>();

    public JsonParser() {
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public T parse(InputStream is, int length) throws ParseException {
        String response = IOUtil.readString(is);
        if (response == null) {
            throw new ParseException("failed to read response");
        }

        Logger.d(response);

        if (length > 0 && response.length() != length) {
            throw new ParseException("broken response");
        }

        final String name = TypeToken.get(getType()).getRawType().getName();
        if ("java.lang.String".equals(name))
            return (T) response;

        String value2parse = response;
        boolean withKey = false;
        boolean withClass = true;

        if (key != null) {
            try {
                JSONObject object = new JSONObject(response);
                value2parse = object.getString(key);
                withClass = false;
            } catch (JSONException e) {
//                throw new ParseException("failed to get key:" + key + " from " + response);
                withKey = true;
                withClass = true;
            }
        }

        if (cls != null && withClass) {
            AnnotationBundle ab = cache.get(cls);
            if (ab == null) {
                ab = new AnnotationBundle();
                ab.extractAnnotation(cls);
                cache.put(cls, ab);
            }

            try {
                JSONObject object = new JSONObject(response);
                String code = ab.getCode(object);
                //请求成功
                if (ab.isSuccess(code)) {
                    value2parse = ab.getData(object, key, withKey);
                } else {
                    //请求失败，抛出业务出错
                    String message = ab.getMessage(object);
                    throw new ParseException(new BusError(code, message, response));
                }
            } catch (JSONException e) {
                throw new ParseException(e.getMessage());
            }
        }

        return new Gson().fromJson(value2parse, getType());
    }

    private static class AnnotationBundle {
        private String code;
        private String successCode;
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

        String getCode(JSONObject o) throws JSONException {
            return o.getString(code);
        }

        String getData(JSONObject o, String key, boolean withKey) throws JSONException {
            final String rs = getNotNull(o, data);

            if (rs == null){
                return o.toString();
            }

            if (withKey) {
                try {
                    JSONObject in = new JSONObject(rs);
                    return in.getString(key);
                } catch (JSONException e) {
                    return rs;
                }
            }
            return rs;
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

        boolean isSuccess(String rp) {
            return successCode.equals(rp);
        }
    }
}
