package com.ddq.net.request;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.ddq.net.error.BaseError;
import com.ddq.net.error.DataError;
import com.ddq.net.error.ErrorState;
import com.ddq.net.view.IProgress;
import com.ddq.net.NetworkClient;
import com.ddq.net.request.callback.DataCallback;
import com.ddq.net.response.parser.BaseParser;
import com.ddq.net.response.parser.TokenHelper;
import com.ddq.net.util.Logger;
import com.ddq.net.util.ThreadPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongdaqing on 2017/6/30.
 */

public class DataRepository implements DataSource {
    public static final String QUERY_CUSTOM_ONLY = "com.ddq.net.oriRequest.QUERY_CUSTOM_ONLY";

    private RemoteDataSource mRemoteDataSource;
    private final List<DataSource> mCustomDataSource;
    private static DataRepository repository;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks;

    private DataRepository() {
        mCustomDataSource = new ArrayList<>();
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

    public void setHost(String host) {
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
    public static DataRepository getRepository(Object object, LocalDataSource custom) {
        getRepository().registerDataSource(object, custom);
        return repository;
    }

    public void registerDataSource(Object object, LocalDataSource customDataSource) {
        final String name = customDataSource.getClass().getName();
        ensureDataSource();
        synchronized (mCustomDataSource) {
            for (int i = 0; i < mCustomDataSource.size(); i++) {
                LocalDataSource c = mCustomDataSource.get(i).mLocalDataSource;
                if (c != null && c.getClass().getName().equals(name)) {
                    throw new RuntimeException("multiple datasource instance:" + name);
                }
            }
            mCustomDataSource.add(new DataSource(object, customDataSource));
        }
    }

    public void unregisterDataSource(LocalDataSource lds) {
        unregisterDataSource(lds.getClass().getName());
    }

    public void unregisterDataSource(String name) {
        ensureDataSource();
        synchronized (mCustomDataSource) {
            for (int i = 0; i < mCustomDataSource.size(); i++) {
                LocalDataSource c = mCustomDataSource.get(i).mLocalDataSource;
                if (c.getClass().getName().equals(name)) {
                    mCustomDataSource.remove(i);
                    break;
                }
            }
        }
    }

    private void ensureDataSource() {
        synchronized (mCustomDataSource) {
            for (int i = 0; i < mCustomDataSource.size(); i++) {
                LocalDataSource c = mCustomDataSource.get(i).mLocalDataSource;
                if (c == null) {
                    mCustomDataSource.remove(i--);
                }
            }
        }
    }

    @Override
    public <T> void request(Params<T> params, Request<T> request) {
        if (mRemoteDataSource == null)
            throw new RuntimeException("remote data source has not been initialized");

        ThreadPool.execute(new RequestRunner<>(params, request));
    }

    private static final int ACTIVITY = 1;
    private static final int FRAGMENT = 2;
    private static final int APPLICATION = 3;

    private class DataSource {
        int type;
        String container;
        LocalDataSource mLocalDataSource;

        public DataSource(Object from, LocalDataSource localDataSource) {
            if (from instanceof Activity) {
                type = ACTIVITY;
                container = from.getClass().getName();
            } else if (from instanceof Fragment) {
                type = FRAGMENT;
                Fragment f = (Fragment) from;
                if (f.getActivity() == null) {
                    throw new RuntimeException("fragment is not attached to activity yet");
                } else {
                    container = f.getActivity().getClass().getName();
                    registerFragmentCallback(f.getActivity());
                }
            } else if (from instanceof Application) {
                type = APPLICATION;
                container = from.getClass().getName();
            } else
                throw new RuntimeException("data source can only be initialed in following components: activity,fragment,application");

            registerActivityCallback(from);
            mLocalDataSource = localDataSource;
        }

        void registerActivityCallback(Object o) {
            if (mActivityLifecycleCallbacks != null)
                return;

            Application app = null;
            if (o instanceof Activity) {
                FragmentActivity activity = (FragmentActivity) o;
                activity.getSupportFragmentManager();
                app = activity.getApplication();
            } else if (o instanceof Fragment) {
                Fragment fragment = (Fragment) o;
                app = fragment.getActivity().getApplication();
            } else if (o instanceof Application) {
                app = (Application) o;
            }

            if (app != null) {
                mActivityLifecycleCallbacks = new ActivityCallback() {
                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        if (activity instanceof FragmentActivity){
                            FragmentActivity fragmentActivity  = (FragmentActivity) activity;
                            fragmentActivity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks);
                        }
                        remove(activity.getClass().getName(), true);
                    }
                };
                app.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            }
        }

        void registerFragmentCallback(final FragmentActivity activity) {
            if (mFragmentLifecycleCallbacks == null) {
                mFragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                        remove(f.getClass().getName(), false);
                    }
                };
                activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, false);
            }
        }

        void remove(String name, boolean activity) {
            synchronized (mCustomDataSource) {
                for (int i = 0; i < mCustomDataSource.size(); i++) {
                    DataSource source = mCustomDataSource.get(i);
                    if (name.equals(source.container)) {
                        mCustomDataSource.remove(i--);
                        if (activity)
                            Logger.d("remove datasource: " + source.mLocalDataSource.getClass().getName() + " before activity " + name + " destroy");
                        else
                            Logger.d("remove datasource: " + source.mLocalDataSource.getClass().getName() + " before fragment " + name + " destroy");
                    }
                }
            }
        }
    }

    private final class RequestRunner<T> implements Runnable {
        private Params<T> params;
        private Request<T> oriRequest;

        public RequestRunner(Params<T> params, Request<T> request) {
            this.params = params;
            this.oriRequest = request;
        }

        @Override
        public void run() {
            IProgress progress = (oriRequest instanceof DataCallback) ? ((DataCallback) oriRequest).getProgress() : null;
            Request<T> request = new UIDataCallback<>(oriRequest, progress);
            request.onStart();
            //类型T的真实type，如果调用段没有设置type就从oriRequest里面获取泛型，
            // 注意这里只有在没有传递泛型的情况下才能获取到正确的内容，具体可以见RequestParams注释
            if (params.params().remove(BaseParser.TYPE_SET) == null)
                params.parser().setType(TokenHelper.getType(oriRequest.getClass()));
            ensureDataSource();
            //如果客户端设置了QUERY_CUSTOM_ONLY，那么就只查询自定义数据源
            boolean localOnly = params.params().remove(QUERY_CUSTOM_ONLY) != null;
            //是否有自定义数据源能处理当前请求
            boolean findCustomSource = false;
            for (int i = 0; i < mCustomDataSource.size(); i++) {
                LocalDataSource lds = mCustomDataSource.get(i).mLocalDataSource;
                if (lds.accept(params)) {
                    lds.request(params, new Faker<T>(params, request));
                    findCustomSource = true;
                    break;
                }
            }

            if (!findCustomSource) {
                if (localOnly) {
                    request.onError(new DataError("no datasource for url: " + params.url()));
                } else {
                    mRemoteDataSource.request(params, request);
                }
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

    private class ActivityCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
