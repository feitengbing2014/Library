package com.ddq.net;

import okhttp3.Headers;

/**
 * Created by dongdaqing on 2017/6/29.
 * 在不同的业务逻辑下，判断Session过期的方法可能不一样，
 * 如果session过期信息不在header里面，就自己写业务逻辑判断，然后自己处理
 */

public interface Session {
    /**
     * 检测session是否过期
     * @param headers
     * @return
     */
    boolean isTimeOut(Headers headers);
}
