package com.ddq.lib.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by dongdaqing on 2017/2/21.
 */

public interface IMessageView {
    void showMessage(@StringRes int res);
    void showMessage(@NonNull String message);
}
