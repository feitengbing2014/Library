package com.ddq.mvp.presenter;

import com.ddq.mvp.view.MvpView;

/**
 * Created by dongdaqing on 2017/7/18.
 */

public interface MvpPresenter<T extends MvpView> {
    void start();

    void stop();
}
