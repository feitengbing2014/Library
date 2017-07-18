package com.ddq.net.view;

import com.ddq.net.error.BaseError;

/**
 * Created by dongdaqing on 2017/2/17.
 */

public interface IErrorView extends IProgress{
    void handleError(BaseError error);
}
