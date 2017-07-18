package com.ddq.net.error;

/**
 * Created by dongdaqing on 2017/6/29.
 * session过期
 */

public class SessionTimeOutError extends BaseError {
    public SessionTimeOutError() {
        super(ErrorState.SESSION_TIME_OUT, null);
    }
}
