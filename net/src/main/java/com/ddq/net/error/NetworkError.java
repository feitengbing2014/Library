package com.ddq.net.error;

public class NetworkError extends BaseError {
    public NetworkError(int errorCode, String message) {
        super(errorCode, message);
    }

    public NetworkError(int errorCode) {
        this(errorCode, null);
    }
}
