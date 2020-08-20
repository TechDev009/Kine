package com.kine.executor

import java.util.concurrent.Executor

class CallbackExecutor:Executor {
    override fun execute(command: Runnable) {
        command.run()
    }
}