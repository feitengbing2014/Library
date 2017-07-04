package com.ddq.architect.view;

import android.support.annotation.UiThread;

import com.ddq.architect.error.BaseError;

/**
 * Created by dongdaqing on 2017/2/17.
 */

public interface IBaseView extends IProgress{
    @UiThread
    void handleError(BaseError error);
}
