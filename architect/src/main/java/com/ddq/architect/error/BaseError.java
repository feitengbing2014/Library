package com.ddq.architect.error;

import android.text.TextUtils;

import static com.ddq.architect.error.ErrorState.BUSINESS_ERROR;
import static com.ddq.architect.error.ErrorState.DEFAULT_ERROR;
import static com.ddq.architect.error.ErrorState.NO_DATA;
import static com.ddq.architect.error.ErrorState.SESSION_TIME_OUT;
import static com.ddq.architect.error.ErrorState.getMsg;

/**
 * Created by dongdaqing on 2017/2/17.
 */

public class BaseError {
    //标记当前错误状态
    private int errorCode;
    private String message;
    private int requestFlag;

    public BaseError(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.message = msg;
        if (TextUtils.isEmpty(msg)) {
            message = getMsg(errorCode);
            if (message == null) {
                message = getMsg(DEFAULT_ERROR);
            }
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(int requestFlag) {
        this.requestFlag = requestFlag;
    }

    /**
     * 前端显示的错误,如果错误码被打开，则会在错误信息后追加错误码
     **/
    public String getMessage() {
        return message;
    }

    public String getOriMessage() {
        return message;
    }

    public boolean isEmptyError() {
        return errorCode == NO_DATA;
    }

    public boolean isBusiError() {
        return errorCode == BUSINESS_ERROR;
    }

    public boolean isSessionTimeOut() {
        return errorCode == SESSION_TIME_OUT;
    }
}
