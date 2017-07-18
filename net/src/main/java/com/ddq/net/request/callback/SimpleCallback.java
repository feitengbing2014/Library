package com.ddq.net.request.callback;

import com.ddq.net.error.BaseError;
import com.ddq.net.view.IErrorView;
import com.ddq.net.view.IProgress;

/**
 * Created by dongdaqing on 2017/4/25.
 */

public abstract class SimpleCallback<R> extends DataCallback<R> {
    private IErrorView mBaseView;

    public SimpleCallback(IErrorView baseView) {
        this(baseView, false);
    }

    public SimpleCallback(IErrorView baseView, boolean withoutProgress) {
        super(withoutProgress ? null : baseView);
        mBaseView = baseView;
    }

    public SimpleCallback(IErrorView baseView, IProgress progress) {
        super(progress);
        mBaseView = baseView;
    }

    @Override
    public final void onError(BaseError error) {
        mBaseView.handleError(error);
    }
}
