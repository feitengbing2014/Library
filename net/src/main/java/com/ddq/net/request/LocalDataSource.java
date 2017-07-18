package com.ddq.net.request;

/**
 * Created by dongdaqing on 2017/7/10.
 */

public interface LocalDataSource extends DataSource {
    <T> boolean accept(Params<T> params);
}
