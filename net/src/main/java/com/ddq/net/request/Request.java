package com.ddq.net.request;

import com.ddq.net.error.BaseError;

/**
 * Created by dongdaqing on 2017/5/5.
 * 请求生命周期
 */

public interface Request<T>{
    /**
     * 标记请求，这里不使用cancel()样式的函数，因为取消操作并不是这个接口的实现类完成的，
     * 而是其他地方完成的，这里就是把其他地方的处理结果放进来而已，简单的做一下标记的意思
     * @param cancel true 表示取消
     */
    void mark(boolean cancel);

    /**
     * 请求开始，可以将动画在这里开始
     */
    void onStart();

    /**
     * 请求成功
     * @param response 请求的响应
     */
    void onSuccess(T response);

    /**
     * 请求出错
     * @param error 错误内容
     */
    void onError(BaseError error);

    /**
     * 请求结束，可以在这里结束动画
     */
    void onFinish();
}
