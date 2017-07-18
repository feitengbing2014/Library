package com.ddq.library;

import com.ddq.net.response.parser.TokenHelper;

/**
 * Created by dongdaqing on 2017/7/7.
 */

public class DdqClass<T> implements AbstractInterface<T>{
    public DdqClass() {
        print();
    }

    @Override
    public void print() {
        System.out.println(TokenHelper.getType(getClass()));
    }

    public static <T> void get(DdqClass<T> ddqClass){
        System.out.println(TokenHelper.getType(new DdqClass<T>(){}.getClass()));
        System.out.println(TokenHelper.getType(ddqClass.getClass()));
    }
}
