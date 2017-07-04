package com.ddq.net.request;

import android.support.annotation.NonNull;

import com.ddq.architect.error.BaseError;
import com.ddq.architect.view.IBaseView;

/**
 * Created by dongdaqing on 2017/4/25.
 */

public abstract class SimpleCallback<R> extends DataCallback<R> {
    private IBaseView mBaseView;

    public SimpleCallback(@NonNull IBaseView baseView) {
        this(baseView, false);
    }

    public SimpleCallback(@NonNull IBaseView baseView, boolean withoutProgress) {
        super(withoutProgress ? null : baseView);
        mBaseView = baseView;
    }

    @Override
    public final void onError(@NonNull BaseError error) {
        mBaseView.handleError(error);
    }
}
