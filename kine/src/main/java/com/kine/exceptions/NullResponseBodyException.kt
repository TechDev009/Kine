package com.kine.exceptions

import java.lang.Exception

class NullResponseBodyException(msg: String?="null response from server"): NullPointerException(msg) {
    constructor(e: Exception):this(e.localizedMessage?:e.message)
}