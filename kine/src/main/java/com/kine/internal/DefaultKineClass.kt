package com.kine.internal

class DefaultKineClass<T>(clazz: Class<T>) :KineClass<T>(clazz){

    override fun isAssignableFrom(t: Class<*>): Boolean {
        return when {
            clazz.isAssignableFrom(t) -> {
                println("Correct type")
                true
            }
            else -> {
                println("Wrong type")
                false
            }
        }

    }
}