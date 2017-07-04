package com.ddq.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by dongdaqing on 2017/6/29.
 */

final class CookieManager {
    private Map<String, String> cookies;

    CookieManager() {
        cookies = new HashMap<>();
    }

    void saveCookie(HttpUrl httpUrl, Cookie cookie) {
        cookies.put(httpUrl.toString(), cookie.name() + "=" + cookie.value());
    }

    List<Cookie> getCookie(HttpUrl httpUrl) {
        List<Cookie> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            Cookie cookie = Cookie.parse(httpUrl, entry.getValue());
            if (cookie != null)
                list.add(cookie);
        }
        return list;
    }
}
