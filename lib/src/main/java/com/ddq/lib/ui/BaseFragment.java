package com.ddq.lib.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.ddq.lib.view.IBroadcastDataView;
import com.ddq.lib.view.IMessageView;
import com.ddq.lib.view.IProgress;
import com.ddq.lib.view.ITransactionView;
import com.ddq.lib.util.FinishOptions;

/**
 * Created by dongdaqing on 2017/6/29.
 */

public class BaseFragment extends Fragment implements IProgress, IMessageView, ITransactionView, IBroadcastDataView {

    @Override
    public void showProgress() {
        IProgress progress = (IProgress) getActivity();
        progress.showProgress();
    }

    @Override
    public void stopProgress() {
        IProgress progress = (IProgress) getActivity();
        progress.stopProgress();
    }

    @Override
    public void broadcast(Intent intent) {
        IBroadcastDataView view = (IBroadcastDataView) getActivity();
        view.broadcast(intent);
    }

    @Override
    public void showMessage(@StringRes int res) {
        IMessageView messageView = (IMessageView) getActivity();
        messageView.showMessage(res);
    }

    @Override
    public void showMessage(@NonNull String message) {
        IMessageView view = (IMessageView) getActivity();
        view.showMessage(message);
    }

    @Override
    public final void finishWithOptions(FinishOptions options) {
        ITransactionView view = (ITransactionView) getActivity();
        view.finishWithOptions(options);
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, FinishOptions options) {
        ITransactionView view = (ITransactionView) getActivity();
        view.toActivity(cls, bundle, options);
    }

    @Override
    public final void toActivity(Intent intent, FinishOptions options) {
        ITransactionView view = (ITransactionView) getActivity();
        view.toActivity(intent, options);
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, int requestCode, FinishOptions options) {
        Intent intent = BaseActivity.getIntentForTransaction(getActivity(), cls, bundle);
        toActivity(intent, requestCode, options);
    }

    @Override
    public final void toActivity(Intent intent, int requestCode, FinishOptions options) {
        startActivityForResult(intent, requestCode);
        if (options != null) {
            if (options.isForwardResult()) {
                getActivity().setResult(options.getResultCode(), options.getData());
            }
            getActivity().finish();
        }
    }

    protected final int getDimensionDelegate(@DimenRes int dimen) {
        return getResources().getDimensionPixelSize(dimen);
    }

    protected final Drawable getDrawableDelegate(@DrawableRes int res) {
        return ContextCompat.getDrawable(getActivity(), res);
    }

    protected final int getColorDelegate(@ColorRes int color) {
        return ContextCompat.getColor(getActivity(), color);
    }
}
