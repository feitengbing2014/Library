package com.ddq.net.request;

import com.ddq.architect.error.BaseError;
import com.ddq.architect.error.DataError;
import com.ddq.architect.error.ErrorState;
import com.ddq.net.NetworkClient;
import com.ddq.net.util.ThreadPool;

/**
 * Created by dongdaqing on 2017/6/30.
 */

public class DataRepository implements DataSource {
    public static final String QUERY_CUSTOM_ONLY = "com.ddq.net.request.QUERY_CUSTOM_ONLY";

    private RemoteDataSource mRemoteDataSource;
    private DataSource mCustomDataSource;
    private static DataRepository repository;

    private DataRepository() {

    }

    public static DataRepository getRepository() {
        if (repository == null)
            repository = new DataRepository();
        return repository;
    }

    public void initial(NetworkClient networkClient) {
        if (mRemoteDataSource == null)
            mRemoteDataSource = new RemoteDataSource(networkClient);
    }

    public void setHost(String host){
        if (mRemoteDataSource != null)
            mRemoteDataSource.getNetworkClient().setHost(host);
    }

    /**
     * get data repository with custom data source
     *
     * @param custom 自定义数据源，可以是本地数据库。
     *               自定义数据源处理请求的规则：
     *               如果查询到数据，按照服务端的格式返回，如果查询不到数据，返回null，
     *               如果查询过程中出现了错误，不想继续走下去，调用setError函数，如果想要继续到服务端查询数据，调用onSuccess函数，参数为null
     *               注意，如果只想查询{@link #mCustomDataSource}，即使查询失败也不向服务端请求的话，可以在查询参数里面以{@link #QUERY_CUSTOM_ONLY}为key设置一个非null的值
     * @return
     */
    public static DataRepository getRepository(DataSource custom) {
        getRepository().mCustomDataSource = custom;
        return repository;
    }

    public void setCustomDataSource(DataSource customDataSource) {
        mCustomDataSource = customDataSource;
    }

    @Override
    public <T> void request(Params<T> params, Request<T> request) {
        if (mRemoteDataSource == null)
            throw new RuntimeException("remote data source has not been initialized");

        ThreadPool.execute(new RequestRunner<>(params, request));
    }

    private final class RequestRunner<T> implements Runnable {
        private Params<T> params;
        private Request<T> request;

        public RequestRunner(Params<T> params, Request<T> request) {
            this.params = params;
            this.request = request;
        }

        @Override
        public void run() {
            request.onStart();
            if (mCustomDataSource != null) {
                mCustomDataSource.request(params, new Faker<T>(params, request));
            } else {
                mRemoteDataSource.request(params, request);
            }
        }
    }

    private class Faker<Q> implements Request<Q> {
        private Params<Q> mRequestParams;
        private Request<Q> mOriginalRequest;

        Faker(Params<Q> requestParams, Request<Q> originalRequest) {
            mRequestParams = requestParams;
            mOriginalRequest = originalRequest;
        }

        @Override
        public void mark(boolean cancel) {
            mOriginalRequest.mark(cancel);
        }

        @Override
        public void onStart() {
            mOriginalRequest.onStart();
        }

        @Override
        public void onSuccess(Q response) {
            if (response == null) {
                boolean proceed = mRequestParams.params().remove(QUERY_CUSTOM_ONLY) == null;
                if (proceed)
                    mRemoteDataSource.request(mRequestParams, mOriginalRequest);
                else
                    onError(new DataError(ErrorState.NO_DATA));
            } else {
                mOriginalRequest.onSuccess(response);
            }
        }

        @Override
        public void onError(BaseError error) {
            mOriginalRequest.onError(error);
        }

        @Override
        public void onFinish() {
            mOriginalRequest.onFinish();
        }
    }
}
