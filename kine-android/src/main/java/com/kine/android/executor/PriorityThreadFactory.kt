package com.kine.android.executor


import android.os.Process
import android.util.Log
import java.util.concurrent.ThreadFactory

class PriorityThreadFactory(private val threadPriority: Int) : ThreadFactory {
    override fun newThread(runnable: Runnable): Thread {
        Log.e("called","android")
        val wrapperRunnable = Runnable {
            try {
                Process.setThreadPriority(threadPriority)
            } catch (t: Throwable) {
            }
            runnable.run()
        }
        return Thread(wrapperRunnable)
    }

}