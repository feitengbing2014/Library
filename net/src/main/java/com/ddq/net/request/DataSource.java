package com.ddq.net.request;

/**
 * Created by dongdaqing on 2017/6/30.
 * retrieve data from data source
 */

public interface DataSource {
    <T> void request(Params<T> params, Request<T> request);
}
