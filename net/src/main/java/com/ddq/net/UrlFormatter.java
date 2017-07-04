package com.ddq.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dongdaqing on 2017/6/29.
 * 用于拼接参数
 */

public class UrlFormatter {
    private static final String UTF_8 = "utf-8";

    public static String appendParams(String base, Map<String, String> params) {
        if (params == null || params.size() == 0)
            return base;

        StringBuilder builder = getBuilder(base);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                builder
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue() == null ? "" : URLEncoder.encode(entry.getValue(), UTF_8))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return builder.substring(0, builder.length() - 1);
    }

    public static String appendParams(String base, JSONObject object) {
        if (object == null || object.length() == 0)
            return base;

        StringBuilder builder = getBuilder(base);
        Iterator<String> iterator = object.keys();
        while (iterator.hasNext()) {
            final String key = iterator.next();
            try {
                final String value = object.getString(key);
                builder
                        .append(key)
                        .append("=")
                        .append(value == null ? "" : URLEncoder.encode(value, UTF_8))
                        .append("&");
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static String getUrl(String host, String url) {
        if (url.startsWith("http://") || url.startsWith("https://"))
            return url;
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }

        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return host + File.separator + url;
    }

    public static String getUrl(String host, String url, Map<String, String> params) {
        String formatted = getUrl(host, url);
        return appendParams(formatted, params);
    }

    public static String getUrl(String host, String url, JSONObject params) {
        String formatted = getUrl(host, url);
        return appendParams(formatted, params);
    }

    private static StringBuilder getBuilder(String base) {
        StringBuilder builder;
        if (base.endsWith("?"))
            builder = new StringBuilder(base);
        else if (base.endsWith("&"))
            builder = new StringBuilder(base.substring(0, base.length() - 1));
        else
            builder = new StringBuilder(base + "?");
        return builder;
    }
}
