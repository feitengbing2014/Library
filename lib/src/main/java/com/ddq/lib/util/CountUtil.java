package com.ddq.lib.util;

import android.os.Handler;

/**
 * Created by dongdaqing on 2017/3/1.
 * 倒计时
 */

public class CountUtil {
    private final Handler mHandler;
    private OnCounting mOnCounting;
    private Counting mCounting;
    private long pausedTime;

    private long timeShift;//时间校准
    private boolean everPaused;
    //剩余时间
    private int hour;
    private int minute;
    private int second;

    public CountUtil(long timeShift, Handler handler, OnCounting onCounting) {
        this.timeShift = timeShift;
        if (handler != null)
            mHandler = handler;
        else
            mHandler = new Handler();
        mOnCounting = onCounting;
        mCounting = new Counting();
    }

    public CountUtil(Handler handler, OnCounting onCounting) {
        this(0, handler, onCounting);
    }

    public CountUtil(OnCounting onCounting) {
        this(null, onCounting);
    }

    public interface OnCounting {
        void counting(int hour, int minute, int second);

        void finishCounting();
    }

    public void setTimeShift(long timeShift) {
        this.timeShift = timeShift;
    }

    public void setOnCounting(OnCounting onCounting) {
        mOnCounting = onCounting;
    }

    public void resetTimeShift() {
        setTimeShift(0);
    }

    public void startWithSeconds(int seconds) {
        start(seconds * DateUtil.SECOND_MILLISECONDS + System.currentTimeMillis());
    }

    public void start(long deadline) {
        calculate(deadline);
        if (canProceed())
            mHandler.post(mCounting);
    }

    public void start() {
        start(-1);
    }

    public void stop() {
        everPaused = false;
        mHandler.removeCallbacks(mCounting);
        hour = 0;
        minute = 0;
        second = 0;
        if (mOnCounting != null)
            mOnCounting.finishCounting();
    }

    private void calculate(long deadline) {
        if (deadline <= 0)
            return;

        long delta = deadline - (System.currentTimeMillis() + timeShift);
        if (delta < 0) {
            stop();
        }

        hour = (int) (delta / DateUtil.HOUR_MILLISECONDS);
        delta -= hour * DateUtil.HOUR_MILLISECONDS;
        minute = (int) (delta / DateUtil.MINUTE_MILLISECONDS);
        delta -= minute * DateUtil.MINUTE_MILLISECONDS;
        second = (int) (delta / DateUtil.SECOND_MILLISECONDS);

        dispatch();
    }

    private void dispatch() {
        if (mOnCounting != null)
            mOnCounting.counting(hour, minute, second);
    }

    private boolean canProceed() {
        return hour + minute + second > 0;
    }

    public void pause() {
        everPaused = true;
        pausedTime = System.currentTimeMillis();
        mHandler.removeCallbacks(mCounting);
    }

    public void resume() {
        if (everPaused) {
            everPaused = false;
            long pausedInterval = System.currentTimeMillis() - pausedTime;
            long stopped = pausedTime + hour * DateUtil.HOUR_MILLISECONDS + minute * DateUtil.MINUTE_MILLISECONDS + second * DateUtil.SECOND_MILLISECONDS;
            start(stopped - pausedInterval);
        }
    }

    private class Counting implements Runnable {

        @Override
        public void run() {
            if (second <= 0) {
                second = 59;
                if (minute <= 0) {
                    minute = 59;
                    hour--;
                } else {
                    minute--;
                }
            } else {
                second--;
            }

            if (canProceed()) {
                dispatch();
                mHandler.post(this);
            } else {
                stop();
            }
        }
    }
}
