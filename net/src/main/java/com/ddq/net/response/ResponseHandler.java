package com.ddq.net.response;

import com.ddq.architect.error.BaseError;
import com.ddq.architect.error.DataError;
import com.ddq.architect.error.ErrorFactory;
import com.ddq.architect.error.ErrorState;
import com.ddq.architect.error.SessionTimeOutError;
import com.ddq.net.DateCalibrate;
import com.ddq.net.Session;
import com.ddq.net.exception.ParseException;
import com.ddq.net.request.Request;
import com.ddq.net.response.parser.Parser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * response响应基类,用于解析服务端返回的数据
 * Created by ddq on 2016/12/6.
 */
public final class ResponseHandler<T> implements okhttp3.Callback {
    private Request<T> mRequest;
    private BaseError mError;
    private Parser<T> mParser;
    private int mRequestFlag;//放到Error里面，用于区分不同请求

    public ResponseHandler(Request<T> request, Parser<T> parser, int requestFlag) {
        mRequest = request;
        mParser = parser;
        mRequestFlag = requestFlag;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        setError(ErrorFactory.getNetworkError(e));//处理网络出错
        dispatchError(call);//传递错误并处理并结束请求
    }

    @Override
    public final void onResponse(Call call, Response response) {
        //计算本地与服务端的时间差
        if (getCalibrate() != null)
            getCalibrate().calibrate(System.currentTimeMillis(), response.header("Date"));

        if (getSession() != null && getSession().isTimeOut(response.headers())) {
            setError(new SessionTimeOutError());
        } else {
            //清空目前已有的error
            setError(null);
            try {
                if (!response.isSuccessful()) {
                    //非成功响应，按照通用错误处理
                    setError(ErrorFactory.getError(response.body().string(), response.code()));
                } else {//服务端成功响应，解析返回数据
                    int length = Integer.parseInt(response.header("Content-Length"));
                    T data = mParser.parse(response.body().byteStream(), length);
                    if (data != null) {
                        mRequest.mark(call.isCanceled());//标记请求
                        mRequest.onSuccess(data);//向上传递数据
                        mRequest.onFinish();//请求结束
                        return;
                    } else {
                        setError(new DataError(ErrorState.NO_DATA));
                    }
                }
            } catch (IOException e) {
                setError(ErrorFactory.getNetworkError(e));//数据读写过程中发生了错误
            } catch (NumberFormatException e) {
                setError(ErrorFactory.getError(e));
            } catch (ParseException e) {
                setError(e.getError());
            }
        }

        dispatchError(call);//向上传递错误并结束请求
    }

    private void setError(BaseError error) {
        this.mError = error;
        if (mError != null) {
            this.mError.setRequestFlag(mRequestFlag);
        }
    }

    /**
     * called setError before this method
     * 这个函数应该作为结束函数调用，这个函数之后这个类里面不应该有其他的处理逻辑
     */
    private void dispatchError(Call call) {
        if (call != null)
            mRequest.mark(call.isCanceled());
        mRequest.onError(mError);
        mRequest.onFinish();//请求结束
    }

    private DateCalibrate getCalibrate() {
        return DateCalibrateManager.getManager().getCalibrate();
    }

    private Session getSession() {
        return SessionManager.getManager().getSessionHandler();
    }
}
