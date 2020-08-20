package com.kine.extensions

import com.kine.executor.KineExecutorManager
import com.kine.request.Request
import com.kine.response.KineError
import com.kine.response.OnError


 fun Any.onCallbackThread(action: () -> Unit) {
    KineExecutorManager.executorSupplier.forCallbackTasks().execute {
        action()
    }
}