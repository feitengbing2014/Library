package com.ddq.net.request.callback;

import com.ddq.net.error.BaseError;
import com.ddq.net.util.Logger;
import com.ddq.net.view.IProgress;

/**
 * Created by dongdaqing on 2017/7/17.
 */

public abstract class IgnoreErrorCallback<R> extends DataCallback<R> {
    public IgnoreErrorCallback() {
    }

    public IgnoreErrorCallback(IProgress progress) {
        super(progress);
    }

    @Override
    public final void onError(BaseError error) {
        if (error.getMessage() != null)
            Logger.d(error.getMessage());
    }
}
