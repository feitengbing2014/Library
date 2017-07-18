package com.ddq.net.error;

/**
 * Created by dongdaqing on 2017/4/25.
 * 业务逻辑出错
 */

public class BusError extends BaseError {
    private String code;//服务端返回的错误代码
    private String response;//服务端返回的完整字符串

    public BusError(String code, String message, String response) {
        super(ErrorState.BUSINESS_ERROR, message);
        this.code = code;
        this.response = response;
    }

    public String getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }
}
