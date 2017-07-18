package com.ddq.lib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ddq.lib.presenter.MvpPresenter;
import com.ddq.lib.view.MvpView;

/**
 * Created by dongdaqing on 2017/7/18.
 */

public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends BaseActivity {
    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    protected void onDestroy() {
        mPresenter.stop();
        super.onDestroy();
    }

    protected abstract P createPresenter();

    public P getPresenter() {
        return mPresenter;
    }
}