package com.kine.exceptions

import java.lang.Exception

class UrlMalformedException(msg: String?="url is not properly formatted or not a url at all"): NullPointerException(msg) {
    constructor(e: Exception):this(e.localizedMessage?:e.message)
}