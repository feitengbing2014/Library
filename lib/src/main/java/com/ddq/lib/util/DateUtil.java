package com.ddq.lib.util;

/**
 * Created by dongdaqing on 2017/3/1.
 * 日期工具类
 */

public class DateUtil {
    public static final int SECOND_MILLISECONDS = 1000;
    public static final int MINUTE_MILLISECONDS = 60 * SECOND_MILLISECONDS;
    public static final int HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;
    public static final int DAY_MILLISECONDS = 24 * HOUR_MILLISECONDS;
    public static final int WEEK_MILLISECONDS = 7 * DAY_MILLISECONDS;

    public static String formatDate(int year, int month, int day, boolean appendZero, char separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append(separator);
        if (month < 10 && appendZero)
            builder.append(0);
        builder.append(month);
        builder.append(separator);
        if (day < 10 && appendZero)
            builder.append(0);
        builder.append(day);
        return builder.toString();
    }

    public static String formatTime(int hour, int minute, int second, boolean appendZero, char separator) {
        StringBuilder builder = new StringBuilder();
        if (hour < 10 && appendZero)
            builder.append(0);
        builder.append(hour);
        builder.append(separator);
        if (minute < 10 && appendZero)
            builder.append(0);
        builder.append(minute);
        builder.append(separator);
        if (second < 10 && appendZero)
            builder.append(0);
        builder.append(second);
        return builder.toString();
    }
}
