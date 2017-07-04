package com.ddq.net.response.parser;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by dongdaqing on 2017/7/3.
 */

public class TokenHelper {
    public static Type getType(Class<?> clazz) {
        String clsName = clazz.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java."))
            return TypeToken.get(Object.class).getType();
        Type superType = clazz.getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            return getSuperclassTypeParameter(clazz);
        } else {
            return getType(clazz.getSuperclass());
        }
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
