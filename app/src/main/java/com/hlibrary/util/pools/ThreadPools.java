package com.hlibrary.util.pools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author linwenhui
 * @date 2019/03/18
 */
public class ThreadPools {

    /**
     * 参数初始化
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数量大小
     */
    private static final int CORE_POOL_SIZE = Math.max(1, Math.min(CPU_COUNT /2, 4));
    /**
     * 线程池最大容纳线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT;
    /**
     * 线程空闲后的存活时长
     */
    private static final int KEEP_ALIVE_TIME = 30;

    /**
     * 任务过多后，存储任务的一个阻塞队列
     */
    private BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();

    /**
     * 线程的创建工厂
     */
    private ThreadFactory threadFactory = new CommonThreadFactory();

    /**
     * 线程池任务满载后采取的任务拒绝策略
     */
    private RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.DiscardOldestPolicy();

    /**
     * 线程池对象，创建线程
     */
    private ThreadPoolExecutor mExecute = null;

    private static ThreadPools instance;
    private static Object lock = new Object();

    private ThreadPools() {
    }

    public static ThreadPools getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ThreadPools();
                }
            }
        }
        return instance;
    }

    private void initPools() {
        if (threadFactory == null) {
            threadFactory = new CommonThreadFactory();
        }
        if (rejectHandler == null) {
            rejectHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
        }
        if (workQueue == null) {
            workQueue = new SynchronousQueue<>();
        }
        if (mExecute == null) {
            mExecute = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS,
                    workQueue,
                    threadFactory,
                    rejectHandler
            );
        }
    }

    public void execute(Runnable runnable) {
        initPools();
        mExecute.execute(runnable);
    }

    public Future submit(Runnable runnable) {
        initPools();
        return mExecute.submit(runnable);

    }

    private class CommonThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AdvacnedAsyncTask #" + mCount.getAndIncrement());
        }
    }


}

















