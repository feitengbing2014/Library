package com.ddq.library;

import com.ddq.net.request.RequestParams;

import java.util.List;

/**
 * Created by dongdaqing on 2017/7/7.
 */

public class Main {
    public static void main(String[] args){
//        System.out.println(TokenHelper.getType(new AbstractClass<User>(){}.getClass()));
//        System.out.println(TokenHelper.getType(new DdqClass<User>(){}.getClass()));
//        System.out.println(TokenHelper.getType(new DdqAbstractClass<User>(){}.getClass()));
//
//        new AbstractClass<User>();
//        new DdqAbstractClass<User>(){};
//        new DdqClass<List<User>>(){};
//        new DdqSubClass<User>(){};

        new RequestParams.Builder<String>(){}.get();
    }
}
