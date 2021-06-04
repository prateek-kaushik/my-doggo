package com.prateek.dogengine

interface UseCaseScheduler {
    fun execute(runnable: Runnable)
}