package com.kine.converters

import com.kine.request.RequestFields
import kotlin.jvm.Throws

interface Converter {
    @Throws(Throwable::class)
    fun <J> convert(response: Any, requestFields: RequestFields, clazz: Class<J>): J?
}