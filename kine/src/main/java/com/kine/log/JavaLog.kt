package com.kine.log

class JavaLog :Log{
    override fun e(tag: String?, message: String, exception: java.lang.Exception?) {
        println("$tag $message "+exception?.localizedMessage)
    }

    override fun v(tag: String?, message: String) {
        println("$tag $message")
    }

    override fun wtf(tag: String?, message: String) {
        println("$tag $message")
    }

    override fun d(tag: String?, message: String) {
        println("$tag $message")
    }

    override fun i(tag: String?, message: String) {
        println("$tag $message")
    }

    override fun w(tag: String?, message: String) {
        println("$tag $message")
    }
}