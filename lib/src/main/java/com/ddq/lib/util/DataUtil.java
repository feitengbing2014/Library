package com.ddq.lib.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by dongdaqing on 2017/2/19.
 * 用于参数校验
 */

public class DataUtil {
    /**
     * 列表是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 数据是否大于0
     * @param s
     * @return
     */
    public static boolean isAboveZero(String s){
        if (TextUtils.isEmpty(s))
            return false;

        try {
            double d = Double.parseDouble(s);
            return d > 0;
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
    }
}
