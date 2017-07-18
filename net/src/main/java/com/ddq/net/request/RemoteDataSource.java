package com.ddq.net.request;

import com.ddq.net.NetworkClient;
import com.ddq.net.error.DataError;
import com.ddq.net.response.ResponseHandler;

/**
 * Created by dongdaqing on 2017/6/30.
 */

class RemoteDataSource implements DataSource {
    private NetworkClient mNetworkClient;

    public RemoteDataSource(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    @Override
    public <T> void request(Params<T> params, Request<T> request) {
        ResponseHandler<T> parser = new ResponseHandler<>(request, params.parser(), params.tag());
        if (params.method() == HttpMethod.GET)
            mNetworkClient.GET(params.tag(), params.url(), params.params(), params.headers(), parser);
        else if (params.method() == HttpMethod.POST) {

        } else {
            request.onError(new DataError("unknown method:" + params.method()));
        }
    }

    public NetworkClient getNetworkClient() {
        return mNetworkClient;
    }

    public void cancelAll() {
        mNetworkClient.cancelAll();
    }
}
