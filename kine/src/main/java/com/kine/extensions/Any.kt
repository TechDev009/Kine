package com.kine.extensions

import com.kine.executor.KineExecutorManager


fun Any.onCallbackThread(action: () -> Unit) {
    KineExecutorManager.executorSupplier.forCallbackTasks().execute {
        action()
    }
}