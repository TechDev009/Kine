package com.kine.log
/**
 * This class is used to filter responses for logging.
 */
object LogLevel {
    const val NO_LEVEL = -2
    const val NO_LOGS = -1
    const val INFO = 0
    const val VERBOSE = 1
    const val DEBUG = 2
    const val WARNING = 3
    const val ERROR = 4
    fun isVerbose(logLevel: Int, localLevel: Int): Boolean {
        return if (localLevel == NO_LEVEL) {
            logLevel >= VERBOSE
        } else localLevel >= VERBOSE
    }

    fun isInfo(logLevel: Int, localLevel: Int): Boolean {
        return if (localLevel == NO_LEVEL) {
            logLevel >= INFO
        } else localLevel >= INFO
    }

    fun isDebug(logLevel: Int, localLevel: Int): Boolean {
        return if (localLevel == NO_LEVEL) {
            logLevel >= DEBUG
        } else localLevel >= DEBUG
    }

    fun isWarning(logLevel: Int, localLevel: Int): Boolean {
        return if (localLevel == NO_LEVEL) {
            logLevel >= WARNING
        } else localLevel >= WARNING
    }

    fun isError(logLevel: Int, localLevel: Int): Boolean {
        return if (localLevel == NO_LEVEL) {
            logLevel >= ERROR
        } else localLevel >= ERROR
    }
}