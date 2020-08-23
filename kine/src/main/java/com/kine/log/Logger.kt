package com.kine.log

import com.kine.extensions.getClassInstance


/**
 * Helper class for logging.
 */
object Logger  {
    private var level = LogLevel.NO_LOGS
    private var disabledLogs = false
    private var localLevel = LogLevel.NO_LEVEL
    private var log:Log= getDefaultLogger()

    private const val androidLoggerClass = "com.kine.android.log.Log"
    private fun getDefaultLogger(): Log {
        return androidLoggerClass.getClassInstance(JavaLog())
    }
    fun setLevel(level: Int) {
        if (level != LogLevel.NO_LEVEL) {
            Logger.level = level
        }
    }

    fun setLocalLevel(level: Int) {
        if (level != LogLevel.NO_LEVEL) {
            localLevel = level
        }
    }

    fun e(tag: String?, message: String,exception: java.lang.Exception?=null) {
        if (LogLevel.isError(level, localLevel) && !disabledLogs) {
            log.e(tag, message,exception)
        }
    }

    fun v(tag: String?, message: String) {
        if (LogLevel.isVerbose(level, localLevel) && !disabledLogs) {
            log.v(tag, message)
        }
    }

    fun wtf(tag: String?, message: String) {
        if (!disabledLogs) {
            log.wtf(tag, message)
        }
    }

    fun d(tag: String?, message: String) {
        if (LogLevel.isDebug(level, localLevel) && !disabledLogs) {
            log.d(tag, message)
        }
    }

    fun i(tag: String?, message: String) {
        if (LogLevel.isInfo(level, localLevel) && !disabledLogs) {
            log.i(tag, message)
        }
    }

    fun w(tag: String?, message: String) {
        if (LogLevel.isWarning(level, localLevel) && !disabledLogs) {
            log.w(tag, message)
        }
    }

    fun printStackTrace(e: Exception) {
        if (!disabledLogs) {
            e.printStackTrace()
        }
    }

    fun setDisabledLogs(disabledLogs: Boolean) {
        Logger.disabledLogs = disabledLogs
    }
}