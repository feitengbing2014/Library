package com.ddq.net.response.parser;

import com.ddq.net.exception.ParseException;

import java.io.InputStream;

/**
 * Created by dongdaqing on 2017/7/3.
 */

public interface Parser<T> {
    /**
     * 解析服务端传回的数据
     * @param is
     * @param length 数据的总长度，从response head 里面取出
     * @return 解析出的结果，null 表示为解析出结果
     * @throws ParseException 解析的过程出现了错误
     */
    T parse(InputStream is, int length) throws ParseException;
}
