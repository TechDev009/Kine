package com.kine.converters

import com.kine.request.Request
import java.io.InputStream
import java.io.Reader

open class RawResponseConverter:Converter {
    override fun <J> convert(response: Any, request: Request, clazz: Class<J>): J? {
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