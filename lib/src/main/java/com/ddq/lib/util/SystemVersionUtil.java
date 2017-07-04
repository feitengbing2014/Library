package com.ddq.lib.util;

import android.os.Build;

/**
 * Created by dongdaqing on 2017/2/17.
 * 系统版本信息
 */

public class SystemVersionUtil {

    public static boolean hasJellyBeanMr1(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasKitkat(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasNougat(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
