package com.ddq.net.request;

import com.ddq.net.response.parser.Parser;

import java.util.Map;

/**
 * Created by dongdaqing on 2017/6/30.
 * 请求参数
 */

public interface Params<T> {
    /**
     * url for request,can not be null
     *
     * @return
     */
    String url();

    /**
     * params for request, or null
     *
     * @return
     */
    Map<String, String> params();

    /**
     * custom additional header
     * @return
     */
    Map<String,String> headers();

    /**
     * request method, default is get
     *
     * @return
     */
    HttpMethod method();

    /**
     * response parser
     * @return
     */
    Parser<T> parser();

    /**
     * tag for request,can use to cancel request
     *
     * @return
     */
    int tag();
}
