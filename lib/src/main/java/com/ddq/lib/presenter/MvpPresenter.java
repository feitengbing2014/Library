package com.ddq.lib.presenter;

import com.ddq.lib.view.MvpView;

/**
 * Created by dongdaqing on 2017/7/18.
 */

public interface MvpPresenter<T extends MvpView> {
    void start();
    void stop();
}
