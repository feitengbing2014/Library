package com.ddq.library;

import com.ddq.net.response.parser.TokenHelper;

/**
 * Created by dongdaqing on 2017/7/7.
 */

public abstract class DdqAbstractClass<T> extends AbstractClass<T> {
    protected void print(){
        System.out.println(TokenHelper.getType(getClass()));
    }
}
