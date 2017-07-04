package com.ddq.net.request;

import com.ddq.architect.view.IProgress;

/**
 * Created by ddq on 2016/12/30.
 * 数据回调接口
 */

public abstract class DataCallback<R> implements Request<R> {
    //请求动画
    private IProgress mProgress;
    private boolean isCanceled;

    /**
     * 没有动画
     **/
    public DataCallback() {}

    public DataCallback(IProgress progress) {
        this.mProgress = progress;
    }

    /**
     * 在回调到UI层的时候判断
     **/
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public void mark(boolean cancel) {
        this.isCanceled = cancel;
    }

    public IProgress getProgress() {
        return mProgress;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onFinish() {
    }
}
