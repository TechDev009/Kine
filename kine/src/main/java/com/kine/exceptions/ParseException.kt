package com.kine.exceptions

import java.lang.Exception

class ParseException(
    msg: String? = "Parsing response failed,check if your specified converter can handle" +
            "the response type and if not set a converter that can handle the needed response type"
) : Exception(msg) {
    constructor(e: Throwable) : this(e.localizedMessage ?: e.message)
}