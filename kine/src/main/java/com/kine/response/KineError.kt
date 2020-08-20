package com.kine.response

import com.kine.exceptions.NoInternetException
import com.kine.exceptions.NullResponseBodyException
import com.kine.exceptions.NullResponseException
import com.kine.exceptions.ParseException

class KineError(val exception:Throwable) {
    fun printStackTrace() {
        exception.printStackTrace()
    }

    fun message(): String? {
        return exception.localizedMessage?:exception.message
    }
    fun isNoInternetError(): Boolean {
        return exception is NoInternetException
    }
    fun isNullResponseError(): Boolean {
        return exception is NullResponseException
    }
    fun isNullResponseBodyError(): Boolean {
        return exception is NullResponseBodyException
    }
    fun isParsingError(): Boolean {
        return exception is ParseException
    }
    fun isHttpStatusCodeError(): Boolean {
        return exception is Exception
    }
}