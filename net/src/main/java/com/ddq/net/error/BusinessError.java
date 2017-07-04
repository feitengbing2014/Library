package com.ddq.net.error;

import com.ddq.architect.error.BaseError;
import com.ddq.architect.error.ErrorState;

/**
 * Created by dongdaqing on 2017/7/4.
 * 业务错误
 */

public class BusinessError extends BaseError {
    private int code;
    //服务端返回的完整字符串
    private String response;

    public BusinessError(int code, String msg, String response) {
        super(ErrorState.BUSINESS_ERROR, msg);
        this.code = code;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }
}
