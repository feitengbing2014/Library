package com.ddq.lib.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dongdaqing on 2017/2/17.
 * 正则校验
 */

public class RegexUtil {
    /**
     *
     * 手机号码
     * 移动：134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
     * 联通：130,131,132,152,155,156,185,186
     * 电信：133,1349,153,180,189
     *   ^[1][3,4,5,7,8][0-9]{9}$
     *
     * 中国移动：China Mobile
     * 134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
     *
     *
     * 中国联通：China Unicom
     * 130,131,132,152,155,156,185,186
     *   ^1(3[0-2]|5[256]|8[56])\\d{8}$
    *
     * 中国电信：China Telecom
     * 133,1349,153,180,189
     *   ^1((33|53|8[09])[0-9]|349)\\d{7}$
    *
     * 大陆地区固话及小灵通
     * 区号：010,020,021,022,023,024,025,027,028,029
     * 号码：七位或八位
     *   ^0(10|2[0-5789]|\\d{3})\\d{7,8}$
     * 网络虚拟号
     * 区号：170
     *   ^17[07]\\d{8}$
     * @param s
     * @return
     */
    public static boolean isMobilePhone(String s){
        if (TextUtils.isEmpty(s))
            return false;

        if (isChinaMobile(s))
            return true;

        if (isChinaUnicom(s))
            return true;

        if (isChinaTelecom(s))
            return true;

        if (isVirtualTelecom(s))
            return true;

        if (isChinaOldNumber(s))
            return true;

        return false;
    }

    private static boolean isChinaMobile(String s){
        Pattern pattern = Pattern.compile("^1(3[4-9]|4[7]|5[0-27-9]|7[08]|8[2-478])\\d{8}$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private static boolean isChinaUnicom(String s){
        Pattern pattern = Pattern.compile("^1(3[0-2]|4[5]|5[56]|7[0156]|8[56])\\d{8}$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private static boolean isChinaTelecom(String s){
        Pattern pattern = Pattern.compile("^1(3[3]|4[9]|53|7[037]|8[019])\\d{8}$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private static boolean isChinaOldNumber(String s){
        Pattern pattern = Pattern.compile("^0(10|2[0-5789]|\\d{3})\\d{7,8}$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private static boolean isVirtualTelecom(String s){
        Pattern pattern = Pattern.compile("^17[07]\\d{8}$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * 是否是身份证号码
     *
     * @param str
     * @return
     */
    public static boolean isIdCardNumber(String str) {
        Pattern p = Pattern
                .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[0-9xX]$");
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
