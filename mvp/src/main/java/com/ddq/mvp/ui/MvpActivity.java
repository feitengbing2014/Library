package com.ddq.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ddq.mvp.presenter.MvpPresenter;
import com.ddq.mvp.view.MvpView;

/**
 * Created by dongdaqing on 2017/7/18.
 */

public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter<MvpView>> extends AppCompatActivity {
    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    public P getPresenter() {
        return mPresenter;
    }
}
