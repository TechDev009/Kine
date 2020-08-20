package com.kine.exceptions

import java.lang.Exception

class HttpStatusCodeException(msg: String?="unsuccessful status code from server ",code:Int): Exception(msg+code) {
    constructor(e: Exception,code:Int):this(e.localizedMessage?:e.message,code)
}