package com.ddq.architect.error;

public class NetworkError extends BaseError {
    public NetworkError(int errorCode, String message) {
        super(errorCode, message);
    }

    public NetworkError(@ErrorState.NetWorkErrorDef int errorCode) {
        this(errorCode, null);
    }
}
