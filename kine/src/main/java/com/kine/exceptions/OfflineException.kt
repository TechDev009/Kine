package com.kine.exceptions

import java.lang.Exception

class OfflineException(msg: String?="request called with offline only mode but no cached response found"): Exception(msg) {
    constructor(e: Exception):this(e.localizedMessage?:e.message)
}