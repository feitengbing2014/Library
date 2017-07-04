package com.ddq.net.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dongdaqing on 2017/7/3.
 */

public class ThreadPool {
    private Executor mExecutor;
    private static ThreadPool pool;

    private ThreadPool() {
        mExecutor = Executors.newFixedThreadPool(2);
    }

    public static void execute(Runnable runnable) {
        if (pool == null)
            pool = new ThreadPool();
        pool.mExecutor.execute(runnable);
    }
}
