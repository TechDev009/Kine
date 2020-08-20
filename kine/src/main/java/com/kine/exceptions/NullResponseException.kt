package com.kine.exceptions

import java.lang.Exception

class NullResponseException(msg: String?="null response from client"): NullPointerException(msg) {
    constructor(e: Exception):this(e.localizedMessage?:e.message)
}