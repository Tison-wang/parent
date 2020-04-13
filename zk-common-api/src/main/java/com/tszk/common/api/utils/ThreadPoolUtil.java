package com.tszk.common.api.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author
 * @date 2020/4/13 14:57
 */
public class ThreadPoolUtil {

    private BlockingQueue<Runnable> taskQueue;

    private ThreadPoolExecutor pool;

    private static class SingleAsyncTaskCall {
        private static ThreadPoolUtil asyncTaskCall = new ThreadPoolUtil();
    }

    /**
     * get the single instance
     */
    public static ThreadPoolUtil getInstance() {
        return SingleAsyncTaskCall.asyncTaskCall;
    }

    public ThreadPoolUtil() {
        int tCoreSize = 1;
        int tMaxSize = 4;
        int tQueueSize = 10000;
        taskQueue = new LinkedBlockingQueue<Runnable>(tQueueSize);
        pool = new ThreadPoolExecutor(tCoreSize, tMaxSize, 30L, TimeUnit.SECONDS, taskQueue);
    }

    public void executeTask(Runnable task) {
        pool.execute(task);
    }
}
