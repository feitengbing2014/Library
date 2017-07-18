package com.ddq.net.util;

import android.util.Log;

/**
 * Created by dongdaqing on 2017/7/3.
 */

public class Logger {
    public static boolean log = true;
    private static final String KEY = "net";

    public static void d(String m) {
        if (log)
            Log.d(KEY, m);
    }

    public static void e(String m) {
        if (log)
            Log.e(KEY, m);
    }
}
