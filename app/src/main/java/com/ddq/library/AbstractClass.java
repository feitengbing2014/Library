package com.ddq.library;

import com.ddq.net.response.parser.TokenHelper;

/**
 * Created by dongdaqing on 2017/7/7.
 */

public class AbstractClass<T> {
    public AbstractClass() {
        print();
    }

    protected void print(){
        System.out.println(TokenHelper.getType(getClass()));
    }
}
