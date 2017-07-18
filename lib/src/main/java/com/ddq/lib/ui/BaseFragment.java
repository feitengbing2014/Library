package com.ddq.lib.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.ddq.lib.util.FinishOptions;
import com.ddq.lib.view.IBroadcastDataView;
import com.ddq.lib.view.ICommitSuccessView;
import com.ddq.lib.view.IMessageView;
import com.ddq.lib.view.IPreferenceView;
import com.ddq.lib.view.IReceiverView;
import com.ddq.lib.view.ITransactionView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dongdaqing on 2017/6/29.
 */

public class BaseFragment extends Fragment implements IMessageView, ITransactionView, IBroadcastDataView, IReceiverView, ICommitSuccessView, IPreferenceView {
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BaseFragment.this.onReceive(intent);
            }
        };
    }

    @Override
    public final void broadcast(Intent intent) {
        IBroadcastDataView view = (IBroadcastDataView) getActivity();
        view.broadcast(intent);
    }

    @Override
    public void showMessage(@StringRes int res) {
        IMessageView messageView = (IMessageView) getActivity();
        if (messageView != null)
            messageView.showMessage(res);
    }

    @Override
    public void showMessage(@NonNull String message) {
        IMessageView view = (IMessageView) getActivity();
        if (view != null)
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

    @Override
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            onActivityResultOk(requestCode, data);
        }
    }

    protected void onActivityResultOk(int requestCode, Intent data) {

    }

    @Override
    public final void register(String... params) {
        if (params == null) return;

        IntentFilter filter = new IntentFilter();
        for (String param : params) {
            filter.addAction(param);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);
    }

    @Override
    public void onReceive(Intent intent) {

    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public final SharedPreferences getPreference(String name) {
        IPreferenceView view = (IPreferenceView) getActivity();
        return view.getPreference(name);
    }

    @Override
    public final SharedPreferences getPreference() {
        IPreferenceView view = (IPreferenceView) getActivity();
        return view.getPreference();
    }

    @Override
    public final void success(String message) {
        ICommitSuccessView view = (ICommitSuccessView) getActivity();
        view.success(message);
    }
}
