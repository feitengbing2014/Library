package com.ddq.net;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by dongdaqing on 2017/6/29.
 * 用于匹配服务端返回的Cookie，需要保存的会被保存下来，下次发起请求会被追加在请求里面
 */

public interface CookieMatcher {
    boolean shouldSaveForLaterUsage(HttpUrl httpUrl, Cookie cookie);
}
