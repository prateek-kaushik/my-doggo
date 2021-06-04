package com.prateek.dogengine

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class UseCaseThreadPoolScheduler : UseCaseScheduler {
    private val POOL_SIZE = Runtime.getRuntime().availableProcessors()

    private val MAX_POOL_SIZE = POOL_SIZE * 2

    private val TIMEOUT = 30L

    private val mThreadPoolExecutor =
        ThreadPoolExecutor(
            POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
            TimeUnit.SECONDS, ArrayBlockingQueue(POOL_SIZE)
        )

    override fun execute(runnable: Runnable) {
        mThreadPoolExecutor.execute(runnable)
    }
}