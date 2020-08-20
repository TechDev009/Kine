package com.kine.android.log

import android.util.Log

class Log:com.kine.log.Log {
    override fun e(tag: String?, message: String, exception: java.lang.Exception?) {
        Log.e(tag, message, exception)
    }

    override fun v(tag: String?, message: String) {
        Log.v(tag, message)

    }

    override fun wtf(tag: String?, message: String) {
        Log.wtf(tag, message)

    }

    override fun d(tag: String?, message: String) {
        Log.d(tag, message)

    }

    override fun i(tag: String?, message: String) {
        Log.i(tag, message)

    }

    override fun w(tag: String?, message: String) {
        Log.w(tag, message)

    }
}