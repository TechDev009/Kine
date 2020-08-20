package com.kine.exceptions

import java.lang.Exception

class TimeoutException (msg: String?="timeout error"): Exception(msg) {
    constructor(e: Exception):this(e.localizedMessage?:e.message)
}