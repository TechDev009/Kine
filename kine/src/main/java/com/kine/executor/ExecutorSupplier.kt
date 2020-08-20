package com.kine.executor

import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

interface ExecutorSupplier {

    fun forNetworkTasks(): ThreadPoolExecutor
    /*
    * returns the thread pool executor for light weight background task
    */
    fun forParsingTasks(): ThreadPoolExecutor
    /*
    * returns the thread pool executor for main thread task
    */
    fun forCallbackTasks(): Executor
}