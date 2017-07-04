package com.ddq.architect.error;

/**
 * Created by dongdaqing on 2017/4/25.
 * 业务逻辑出错
 */

public class BusError extends BaseError {
    private String code;
    private String data;

    public BusError(String code, String message, String data) {
        super(ErrorState.BUSINESS_ERROR, message);
        this.code = code;
        this.data = data;
    }
}
