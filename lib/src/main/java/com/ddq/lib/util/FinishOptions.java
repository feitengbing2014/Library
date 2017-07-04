package com.ddq.lib.util;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by dongdaqing on 2017/5/15.
 */

public class FinishOptions {
    /**
     * 是否调用setResult(int,intent)方法
     */
    private boolean forwardResult;
    /**
     * 调用setResult方法的参数，即返回前一个界面的数据
     */
    private Intent data;
    private int resultCode;

    private FinishOptions() {
        forwardResult = false;
    }

    public static FinishOptions FINISH_ONLY() {
        FinishOptions options = new FinishOptions();
        options.forwardResult = false;
        return options;
    }

    public static FinishOptions FORWARD_RESULT() {
        return FORWARD_RESULT(null, Activity.RESULT_OK);
    }

    public static FinishOptions FORWARD_RESULT(Intent data) {
        return FORWARD_RESULT(data, Activity.RESULT_OK);
    }

    public static FinishOptions FORWARD_RESULT(Intent data, int resultCode) {
        FinishOptions options = new FinishOptions();
        options.forwardResult = true;
        options.data = data;
        options.resultCode = resultCode;
        return options;
    }

    public boolean isForwardResult() {
        return forwardResult;
    }

    public Intent getData() {
        return data;
    }

    public int getResultCode() {
        return resultCode;
    }
}
