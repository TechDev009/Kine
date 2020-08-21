package com.kine.internal


/**
 * Helper class for checking generic instance type .
 */
open class KineClass<T>(val clazz: Class<T>) {
    companion object {
        inline operator fun <reified T : Any>invoke() =
            KineClass(T::class.java)
    }

    open fun isAssignableFrom(t: Any): Boolean {
        return isAssignableFrom(t.javaClass)
    }
   open fun isAssignableFrom(t: Class<*>): Boolean {
       return clazz.isAssignableFrom(t).apply {
       //    println(if (this) "Correct type" else "Wrong type")
       }
    }
}