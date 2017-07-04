package com.ddq.net.response;

import com.ddq.net.DateCalibrate;

import java.lang.ref.WeakReference;

/**
 * Created by dongdaqing on 2017/6/30.
 */

public class DateCalibrateManager {
    private WeakReference<DateCalibrate> mCalibrate;
    private static DateCalibrateManager manager;
    private DateCalibrateManager(){

    }

    static DateCalibrateManager getManager(){
        if (manager == null)
            manager = new DateCalibrateManager();
        return manager;
    }

    public static void setDateCalibrater(DateCalibrate m){
        getManager().mCalibrate = new WeakReference<>(m);
    }

    public static void clear(){
        getManager().mCalibrate = null;
    }

    DateCalibrate getCalibrate() {
        return mCalibrate != null ? mCalibrate.get() : null;
    }
}
