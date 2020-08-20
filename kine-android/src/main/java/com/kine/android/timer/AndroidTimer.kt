package com.kine.android.timer

import android.os.SystemClock
import com.kine.timer.Timer

class AndroidTimer :Timer{
    var time:Long=0
    override fun new(): Timer {
        return AndroidTimer()
    }

    override fun start() {
        time = SystemClock.elapsedRealtime()
    }

    override fun stop(): Long {
        return SystemClock.elapsedRealtime() - time
    }

    override fun time(): Long {
        return SystemClock.elapsedRealtime()
    }

}