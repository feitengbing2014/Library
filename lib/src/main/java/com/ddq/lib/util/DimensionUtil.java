package com.ddq.lib.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by dongdaqing on 2017/6/29.
 * 常用的dp转px
 */

public class DimensionUtil {
    public static int dp2px(Context context, float dp) {
        DisplayMetrics dm = new DisplayMetrics();
        return (int) (dp * dm.density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        DisplayMetrics dm = new DisplayMetrics();
        return (int) (pxValue / dm.density + 0.5f);
    }
}
