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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ddq.lib.util.FinishOptions;
import com.ddq.lib.view.IBroadcastDataView;
import com.ddq.lib.view.ICommitSuccessView;
import com.ddq.lib.view.IMessageView;
import com.ddq.lib.view.IPreferenceView;
import com.ddq.lib.view.IReceiverView;
import com.ddq.lib.view.ITransactionView;


/**
 * Created by dongdaqing on 2017/2/17.
 * 这里提供了很多基础功能方法，开发至今总结而来
 */

public abstract class BaseActivity extends AppCompatActivity implements IMessageView, ITransactionView, IBroadcastDataView, IReceiverView, ICommitSuccessView, IPreferenceView {

    private Toast mToast;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BaseActivity.this.onReceive(intent);
            }
        };
    }

    protected final int getDimensionDelegate(@DimenRes int dimen) {
        return getResources().getDimensionPixelSize(dimen);
    }

    protected final Drawable getDrawableDelegate(@DrawableRes int res) {
        return ContextCompat.getDrawable(this, res);
    }

    protected final int getColorDelegate(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    @Override
    public void showMessage(@StringRes int res) {
        showMessageWithToast(getString(res));
    }

    @Override
    public void showMessage(@NonNull String message) {
        showMessageWithToast(message);
    }

    private void showMessageWithToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }

//    @Override
//    public void showProgress() {
//        if (mDialog == null) {
//            mDialog = new ProgressDialog(this);
//            mDialog.setIndeterminate(true);
//            mDialog.setCancelable(false);
//        }
//        if (!mDialog.isShowing())
//            mDialog.show();
//    }
//
//    @Override
//    public void stopProgress() {
//        if (mDialog != null && mDialog.isShowing())
//            mDialog.dismiss();
//    }

    @Override
    public final void broadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public final void finishWithOptions(FinishOptions options) {
        if (options != null && options.isForwardResult()) {
            setResult(options.getResultCode(), options.getData());
        }
        finish();
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, FinishOptions options) {
        toActivity(getIntentForTransaction(this, cls, bundle), options);
    }

    @Override
    public final void toActivity(Intent intent, FinishOptions options) {
        startActivity(intent);
        if (options != null) {
            if (options.isForwardResult()) {
                setResult(options.getResultCode(), options.getData());
            }
            finish();
        }
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, int requestCode, FinishOptions options) {
        toActivity(getIntentForTransaction(this, cls, bundle), requestCode, options);
    }

    @Override
    public final void toActivity(Intent intent, int requestCode, FinishOptions options) {
        startActivityForResult(intent, requestCode);
        if (options != null) {
            if (options.isForwardResult()) {
                setResult(options.getResultCode(), options.getData());
            }
            finish();
        }
    }

    public static Intent getIntentForTransaction(Context context, Class cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        return intent;
    }

    @Override
    public final SharedPreferences getPreference(String name) {
        return getSharedPreferences(name, MODE_PRIVATE);
    }

    @Override
    public final SharedPreferences getPreference() {
        return getSharedPreferences(getClass().getName(), MODE_PRIVATE);
    }

    @Override
    public final void success(String message) {
        showMessage(message);
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    @Override
    public void onReceive(Intent intent) {

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
