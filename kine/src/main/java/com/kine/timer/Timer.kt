package com.kine.timer

interface Timer {

    fun clone():Timer

    fun start()

    fun stop():Long

    fun time():Long
}