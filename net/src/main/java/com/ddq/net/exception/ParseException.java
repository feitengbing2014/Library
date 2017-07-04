package com.ddq.net.exception;

import com.ddq.architect.error.BaseError;
import com.ddq.architect.error.DataError;

/**
 * Created by dongdaqing on 2017/7/3.
 * {@link com.ddq.net.response.parser.Parser}解析服务端返回的
 * 数据如果发生了错误，用本类抛出异常
 */

public class ParseException extends Exception {
    private BaseError mError;

    public ParseException(String message) {
        super(message);
        mError = new DataError(message);
    }

    public ParseException(BaseError error) {
        mError = error;
    }

    public BaseError getError() {
        return mError;
    }
}
