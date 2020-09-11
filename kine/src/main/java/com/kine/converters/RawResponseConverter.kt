package com.kine.converters

import com.kine.request.RequestFields
import java.io.InputStream
import java.io.Reader

open class RawResponseConverter:Converter {
    @Suppress("UNCHECKED_CAST")
    override fun <J> convert(response: Any, requestFields: RequestFields, clazz: Class<J>): J? {
        return when  {
            response is String && clazz.isAssignableFrom(String::class.java) -> {
                response as J
            }
            response is ByteArray && clazz.isAssignableFrom(ByteArray::class.java) -> {
                response as J
            }
            response is InputStream && clazz.isAssignableFrom(InputStream::class.java) -> {
                response as J
            }
            response is Reader && clazz.isAssignableFrom(Reader::class.java) -> {
                response as J
            }
            else -> {
                null
            }
        }
    }
}