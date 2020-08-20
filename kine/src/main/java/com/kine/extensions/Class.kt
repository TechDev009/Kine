package com.kine.extensions

fun <T,F:T> String.getClassInstance(default:F): T {
    return this.getClassInstanceOrNull<T>()?:default
}
fun <T> String.getClassInstanceOrNull(): T? {
    return try {
        Class.forName(this).newInstance() as T
    }catch (exception: ClassNotFoundException) {
        exception.printStackTrace()
        null
    }
}