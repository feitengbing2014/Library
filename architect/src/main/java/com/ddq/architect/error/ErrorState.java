package com.ddq.architect.error;

import android.support.annotation.IntDef;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 错误码
 * Created by cc on 2017/2/21.
 */
public class ErrorState {
    //数据错误
    public static final int DEFAULT_ERROR = -1100;
    public static final int QUERY_PARAMS_ERROR = -1200;
    public static final int UNKNOWN_DATA = -1300;
    public static final int NOT_FOUND = -1400;
    //客户端向服务端发起请求，服务端返回业务错误
    public static final int BUSINESS_ERROR = -1500;
    //列表数据为空
    public static final int NO_DATA = -1600;
    public static final int SESSION_TIME_OUT = -1700;
    public static final int AUTH_FAILED_ERROR = -1800;

    //服务出错，网络问题等
    public static final int TIME_OUT = -2000;
    public static final int UNKNOWN_HOST = -2100;
    public static final int NETWORK_ERROR = -2200;
    public static final int SERVER_ERROR = -2300;
    //服务器宕机
    public static final int NO_ROUTE_TO_HOST_ERROR = -2400;

    public static final int BUSNIESS_ERROR = -2500;

    private static final SparseArray<String> errorMsg = new SparseArray<>();

    static {
        errorMsg.put(DEFAULT_ERROR, "操作失败");
        errorMsg.put(QUERY_PARAMS_ERROR, "请求参数出错");
        errorMsg.put(UNKNOWN_DATA, "数据无法解析");
        errorMsg.put(NO_DATA, "暂无数据");
        errorMsg.put(NOT_FOUND, "请求的数据或者接口找不到");
        errorMsg.put(BUSINESS_ERROR, "返回出错");
        errorMsg.put(SESSION_TIME_OUT, "会话过期,请重新登录");
        errorMsg.put(AUTH_FAILED_ERROR, "验证出错");

        errorMsg.put(TIME_OUT, "请求超时");
        errorMsg.put(UNKNOWN_HOST, "网络异常");
        errorMsg.put(NETWORK_ERROR, "网络异常");
        errorMsg.put(SERVER_ERROR, "服务异常");
        errorMsg.put(NO_ROUTE_TO_HOST_ERROR, "服务端繁忙");
    }

    public static String getMsg(int errorCode) {
        return errorMsg.get(errorCode);
    }
    @IntDef({DEFAULT_ERROR, QUERY_PARAMS_ERROR, UNKNOWN_DATA, NOT_FOUND, BUSINESS_ERROR, NO_DATA, SESSION_TIME_OUT, AUTH_FAILED_ERROR,TIME_OUT, UNKNOWN_HOST, NETWORK_ERROR, SERVER_ERROR, NO_ROUTE_TO_HOST_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorDef {
    }
    @IntDef({DEFAULT_ERROR, QUERY_PARAMS_ERROR, UNKNOWN_DATA, NOT_FOUND, BUSINESS_ERROR, NO_DATA, SESSION_TIME_OUT, AUTH_FAILED_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DataErrorDef {
    }

    @IntDef({TIME_OUT, UNKNOWN_HOST, NETWORK_ERROR, SERVER_ERROR, NO_ROUTE_TO_HOST_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetWorkErrorDef {
    }
}
