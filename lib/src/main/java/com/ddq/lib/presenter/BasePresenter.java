package com.ddq.lib.presenter;

import com.ddq.lib.view.MvpView;

/**
 * Created by dongdaqing on 2017/7/18.
 */

public class BasePresenter<V extends MvpView>{
    private V mView;

    public BasePresenter(V view) {
        mView = view;
    }

    public V getView() {
        return mView;
    }

    public void start() {

    }

    public void stop() {

    }
}
