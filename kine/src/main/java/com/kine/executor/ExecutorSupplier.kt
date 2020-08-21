package com.kine.executor

import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

interface ExecutorSupplier {
    /*
      * returns the thread pool executor for networking task
      */
    fun forNetworkTasks(): ThreadPoolExecutor

    /*
    * returns the thread pool executor for light weight background task like parsing
    */
    fun forParsingTasks(): ThreadPoolExecutor

    /*
    * returns the thread pool executor for executing callback on (Main Thread in Android case)
    */
    fun forCallbackTasks(): Executor
}