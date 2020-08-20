package com.kine.exceptions

import java.lang.Exception

class UnknownException(msg: String?="unknown error"): Exception(msg) {
    constructor(e: Exception):this(e.localizedMessage?:e.message)
}