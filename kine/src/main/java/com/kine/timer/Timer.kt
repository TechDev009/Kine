package com.kine.timer

interface Timer {

    fun new():Timer

    fun start()

    fun stop():Long

    fun time():Long
}