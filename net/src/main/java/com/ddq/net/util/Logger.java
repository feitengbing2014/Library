package com.ddq.net.util;

import android.util.Log;

/**
 * Created by dongdaqing on 2017/7/3.
 */

public class Logger {
    private static final String KEY = "net";

    public static void d(String m) {
        Log.d(KEY, m);
    }

    public static void e(String m) {
        Log.e(KEY, m);
    }
}
