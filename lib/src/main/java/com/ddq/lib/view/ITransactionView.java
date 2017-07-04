package com.ddq.lib.view;

import android.content.Intent;
import android.os.Bundle;

import com.ddq.lib.util.FinishOptions;

/**
 * Created by dongdaqing on 2017/5/15.
 * 用于界面切换，在Activity和Fragment的基类实现
 */

public interface ITransactionView {
    /**
     * 结束当前界面
     *
     * @param options 为null表示仅结束界面
     */
    void finishWithOptions(FinishOptions options);

    /**
     * 界面跳转
     *
     * @param cls
     * @param bundle
     * @param options 为null表示不要结束当前界面，跟{@link #finishWithOptions(FinishOptions)}不一样
     */
    void toActivity(Class cls, Bundle bundle, FinishOptions options);

    /**
     * 同上
     * @param intent
     * @param options
     */
    void toActivity(Intent intent, FinishOptions options);

    /**
     * 同上
     *
     * @param cls
     * @param bundle
     * @param requestCode
     * @param options     为null表示不要结束当前界面，跟{@link #finishWithOptions(FinishOptions)}不一样
     */
    void toActivity(Class cls, Bundle bundle, int requestCode, FinishOptions options);

    /**
     * 同上
     * @param intent
     * @param requestCode
     * @param options
     */
    void toActivity(Intent intent, int requestCode, FinishOptions options);
}
