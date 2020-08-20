package com.kine.android.executor

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor


class MainThreadExecutor : Executor {
    private val handler: Handler = Handler(Looper.getMainLooper())
    override fun execute(runnable: Runnable) {
        val isUiThread = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        Looper.getMainLooper().isCurrentThread
        else Thread.currentThread() == Looper.getMainLooper().thread
        if(isUiThread){
            runnable.run()
        }else {
            handler.post(runnable)
        }
    }

}