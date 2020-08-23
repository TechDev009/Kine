package com.kine.executor

import com.kine.extensions.getClassInstance


object KineExecutorManager {
    val executorSupplier: ExecutorSupplier = getDefaultExecutorSupplier()
    private const val androidExecutorClass = "com.kine.android.executor.AndroidPriorityExecutorSupplier"
    private fun getDefaultExecutorSupplier(): ExecutorSupplier {
           return androidExecutorClass.getClassInstance(DefaultPriorityExecutorSupplier())
    }
}