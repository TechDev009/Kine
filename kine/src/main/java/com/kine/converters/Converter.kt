package com.kine.converters

import com.kine.request.Request
import kotlin.jvm.Throws

interface Converter {
    @Throws(Throwable::class)
    fun <J> convert(response: Any, request: Request, clazz: Class<J>): J?
}