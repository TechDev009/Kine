package com.kine.executor

import com.kine.policies.Priority

open class PriorityRunnable(private val priority: Priority) : Runnable {
    override fun run() {
        // nothing to do here.
    }

    fun getPriority(): Priority {
        return priority
    }

}