package com.kine.timer

class SimpleTimer:Timer {
    var time:Long=0
    override fun clone(): Timer {
       return SimpleTimer()
    }

    override fun start() {
        time = System.currentTimeMillis()
    }

    override fun stop(): Long {
        return System.currentTimeMillis() - time
    }

    override fun time(): Long {
        return System.currentTimeMillis()
    }
}