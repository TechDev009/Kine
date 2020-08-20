package com.kine.executor


object KineExecutorManager {
    val executorSupplier: ExecutorSupplier = getDefaultExecutorSupplier()
    private const val androidExecutorClass = "com.kine.android.executor.AndroidPriorityExecutorSupplier"
    private fun getDefaultExecutorSupplier(): ExecutorSupplier {
        return try {
            Class.forName(androidExecutorClass).newInstance() as ExecutorSupplier
        } catch (exception: ClassNotFoundException) {
            exception.printStackTrace()
            DefaultPriorityExecutorSupplier()
        }
    }
}