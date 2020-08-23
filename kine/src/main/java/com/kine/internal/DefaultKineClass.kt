package com.kine.internal

class DefaultKineClass<T>(clazz: Class<T>) : KineClass<T>(clazz) {

    override fun isAssignableFrom(t: Class<*>): Boolean {
        return clazz.isAssignableFrom(t).apply {
        }
    }
}