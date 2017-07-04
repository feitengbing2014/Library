package com.ddq.architect.error;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by ddq on 2016/12/7.
 */
public class ErrorFactory {

    public static BaseError getError(Exception e) {
        if (e instanceof IOException) {
            return getNetworkError((IOException) e);
        }
        return new DataError(ErrorState.UNKNOWN_DATA);
    }

    public static NetworkError getNetworkError(IOException e) {
        if (e instanceof UnknownHostException) {
            return new NetworkError(ErrorState.UNKNOWN_HOST);
        } else if (e instanceof ConnectException) {
            return new NetworkError(ErrorState.NETWORK_ERROR);
        } else if (e instanceof NoRouteToHostException) {
            return new NetworkError(ErrorState.NO_ROUTE_TO_HOST_ERROR);
        } else if (e instanceof SocketException) {
            return new NetworkError(ErrorState.SERVER_ERROR);
        } else if (e instanceof SocketTimeoutException) {
            return new NetworkError(ErrorState.TIME_OUT);
        } else {
            return new NetworkError(ErrorState.SERVER_ERROR);
        }
    }

    /**
     * 有响应时
     *
     * @param responseStr
     * @param httpCode
     * @return
     */
    public static DataError getError(String responseStr, int httpCode) {
        //追加所需错误
        if (httpCode == 404) {
            return new DataError(ErrorState.NOT_FOUND);
        }else {
            return new DataError(ErrorState.UNKNOWN_DATA, null, responseStr);
        }
    }

    public static DataError paramError(String message) {
        return new DataError(ErrorState.QUERY_PARAMS_ERROR, message);
    }

    public static DataError emptyDataError(String message) {
        return new DataError(ErrorState.NO_DATA, message);
    }

    public static DataError emptyDataError() {
        return new DataError(ErrorState.NO_DATA);
    }
}
