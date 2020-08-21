package com.kine.timer

object TimerManager {
    private var timer: Timer = SimpleTimer()

    fun start(): Timer {
        return timer.clone().apply { start()}
    }

    fun time(): Long {
       return timer.time()
    }
}