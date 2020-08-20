package com.kine.executor


import java.util.concurrent.ThreadFactory

class PriorityThreadFactory(private val threadPriority: Int) : ThreadFactory {
    override fun newThread(runnable: Runnable): Thread {
        return Thread(runnable).apply { priority = threadPriority }
    }

}