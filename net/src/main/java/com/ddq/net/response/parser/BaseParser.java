package com.ddq.net.response.parser;

import java.lang.reflect.Type;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public abstract class BaseParser<T> implements Parser<T>{
    public static final String TYPE_SET = "com.ddq.net.response.parser_TYPE_SET";
    private Type mType;
    @Override
    public void setType(Type type) {
        this.mType = type;
    }

    public Type getType() {
        return mType;
    }
}
