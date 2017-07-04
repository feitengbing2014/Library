package com.ddq.net.request;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.ddq.architect.error.BaseError;

/**
 * Created by ddq on 2016/12/30.
 * 负责将子线程数据发送到主线程处理
 */
public final class UIDataCallback<R> extends DataCallback<R> {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private Request<R> mRequest;

    public UIDataCallback(DataCallback<R> request) {
        super(request == null ? null : request.getProgress());
        this.mRequest = request;
    }

    @Override
    public void onStart() {
        HANDLER.post(new Start());
    }

    @Override
    public void onSuccess(@NonNull R response) {
        HANDLER.post(new Success(response));
    }

    @Override
    public void onError(@NonNull BaseError error) {
        HANDLER.post(new Failed(error));
    }

    @Override
    public void onFinish() {
        HANDLER.post(new Finished());
    }

    private class Start implements Runnable {
        @Override
        public void run() {
            mRequest.onStart();//调用自己的onStart方法表示请求开始
            if (getProgress() != null/*in case*/ && !isCanceled()) {
                getProgress().showProgress();
            }
        }
    }

    private class Success implements Runnable {
        private R mResponse;

        Success(R response) {
            this.mResponse = response;
        }

        @Override
        public void run() {
            if (!isCanceled() && mRequest != null) {
                //请求成功
                mRequest.onSuccess(mResponse);
            }
        }
    }

    private class Failed implements Runnable {
        private BaseError mError;

        Failed(BaseError error) {
            this.mError = error;
        }

        @Override
        public void run() {
            if (!isCanceled() && mRequest != null) {
                //请求失败
                mRequest.onError(mError);
            }
        }
    }

    private class Finished implements Runnable {

        @Override
        public void run() {
            if (mRequest != null)
                mRequest.onFinish();

            if (getProgress() != null)
                getProgress().stopProgress();
        }
    }
}
