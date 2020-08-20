package com.kine.log

interface Log {
    fun e(tag: String?, message: String, exception: java.lang.Exception? = null) {
        println("$tag $message "+exception?.localizedMessage)
    }

    fun v(tag: String?, message: String) {
        println("$tag $message")
    }

    fun wtf(tag: String?, message: String) {
        println("$tag $message")
    }

    fun d(tag: String?, message: String) {
        println("$tag $message")
    }

    fun i(tag: String?, message: String) {
        println("$tag $message")
    }

    fun w(tag: String?, message: String) {
        println("$tag $message")
    }
}