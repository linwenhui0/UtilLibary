package com.hlibrary.util.pools

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

/**
 * @author linwenhui
 * @date 2019/03/18
 */
class ThreadPools private constructor() {

    /**
     * 任务过多后，存储任务的一个阻塞队列
     */
    private var workQueue: BlockingQueue<Runnable>? = SynchronousQueue()

    /**
     * 线程的创建工厂
     */
    private var threadFactory: ThreadFactory? = CommonThreadFactory()

    /**
     * 线程池任务满载后采取的任务拒绝策略
     */
    private var rejectHandler: RejectedExecutionHandler? = ThreadPoolExecutor.DiscardOldestPolicy()

    /**
     * 线程池对象，创建线程
     */
    private var mExecute: ThreadPoolExecutor? = null

    private fun initPools() {
        if (threadFactory == null) {
            threadFactory = CommonThreadFactory()
        }
        if (rejectHandler == null) {
            rejectHandler = ThreadPoolExecutor.DiscardOldestPolicy()
        }
        if (workQueue == null) {
            workQueue = SynchronousQueue()
        }
        if (mExecute == null) {
            mExecute = ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE_TIME.toLong(),
                    TimeUnit.SECONDS,
                    workQueue,
                    threadFactory,
                    rejectHandler
            )
        }
    }

    fun execute(runnable: Runnable) {
        initPools()
        mExecute!!.execute(runnable)
    }

    fun submit(runnable: Runnable): Future<*> {
        initPools()
        return mExecute!!.submit(runnable)

    }

    private inner class CommonThreadFactory : ThreadFactory {
        private val mCount = AtomicInteger(1)

        override fun newThread(r: Runnable): Thread {
            return Thread(r, "AdvacnedAsyncTask #" + mCount.getAndIncrement())
        }
    }

    companion object {

        /**
         * 参数初始化
         */
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        /**
         * 核心线程数量大小
         */
        private val CORE_POOL_SIZE = max(1, min(CPU_COUNT / 2, 4))
        /**
         * 线程池最大容纳线程数
         */
        private val MAXIMUM_POOL_SIZE = CPU_COUNT
        /**
         * 线程空闲后的存活时长
         */
        private const val KEEP_ALIVE_TIME = 30

        val instance: ThreadPools by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ThreadPools()
        }


    }


}

















