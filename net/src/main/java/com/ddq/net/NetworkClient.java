package com.ddq.net;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dongdaqing on 2017/6/29.
 * 封装okhttp调用，简化网络请求
 */

public final class NetworkClient {
    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private static final int DEFAULT_READ_TIMEOUT = 30;
    private static final int DEFAULT_WRITE_TIMEOUT = 30;
    private OkHttpClient mClient;
    private String host;

    private NetworkClient() {
    }

    public static NetworkClient HTTP(CookieMatcher matcher) {
        NetworkClient client = new NetworkClient();
        client.mClient = builder(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, matcher).build();
        return client;
    }

    public static NetworkClient HTTP(long connectTimeout, long readTimeout, long writeTimeout, CookieMatcher matcher) {
        NetworkClient client = new NetworkClient();
        client.mClient = builder(connectTimeout, readTimeout, writeTimeout, matcher).build();
        return client;
    }

    public static NetworkClient HTTPS(CookieMatcher matcher, SSLSocketFactory factory, X509TrustManager manager, HostnameVerifier verifier) {
        return HTTPS(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, matcher, factory, manager, verifier);
    }

    public static NetworkClient HTTPS(long connectTimeout, long readTimeout, long writeTimeout, CookieMatcher matcher, SSLSocketFactory factory, X509TrustManager manager, HostnameVerifier verifier) {
        NetworkClient client = new NetworkClient();
        client.mClient = builder(connectTimeout, readTimeout, writeTimeout, matcher)
                .sslSocketFactory(factory, manager)
                .hostnameVerifier(verifier)
                .build();
        return client;
    }

    private static OkHttpClient.Builder builder(long connectTimeout, long readTimeout, long writeTimeout, CookieMatcher matcher) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .cookieJar(new CJ(matcher));
        return builder;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 执行GET请求
     * @param hashCodeTag 请求标记
     * @param url 请求url
     * @param requestJson 请求参数
     * @param headers 自定义HTTP头
     * @param callback 回调接口
     */
    public void GET(int hashCodeTag, String url, Map<String, String> requestJson, Map<String, String> headers, Callback callback) {
        Request.Builder builder = new Request.Builder().tag(hashCodeTag).url(UrlFormatter.getUrl(host, url, requestJson));
        request(builder, headers, callback);
    }

    public void cancelAll() {
        mClient.dispatcher().cancelAll();
    }

    private void request(Request.Builder builder, Map<String, String> headers, Callback callback) {
        if (headers != null) {
            Headers.Builder hb = new Headers.Builder();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                hb.add(entry.getKey(), entry.getValue());
            }
            builder.headers(hb.build());
        }

        //根据Request对象发起Get异步Http请求，并添加请求回调
        Call call = mClient.newCall(builder.build());
        call.enqueue(callback);
    }

    /**
     * 用来管理Cookie
     */
    private static class CJ implements CookieJar {
        private CookieMatcher mCookieMatcher;
        private CookieManager mCookieManager;

        CJ(CookieMatcher cookieMatcher) {
            mCookieMatcher = cookieMatcher;
            mCookieManager = new CookieManager();
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (mCookieMatcher == null)
                return;

            for (Cookie cookie : cookies) {
                if (mCookieMatcher.shouldSaveForLaterUsage(url, cookie)) {
                    mCookieManager.saveCookie(url, cookie);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return mCookieManager.getCookie(url);
        }
    }
}
