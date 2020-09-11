@file:Suppress("UNCHECKED_CAST")

package com.kine.converters

import com.kine.request.RequestFields
import okio.BufferedSource
import okio.ByteString


class OkioConverter : RawResponseConverter() {
    override fun <J> convert(response: Any, requestFields: RequestFields, clazz: Class<J>): J? {
        return when {
            response is ByteString && clazz.isAssignableFrom(ByteString::class.java) -> {
                response as J
            }
            response is BufferedSource && clazz.isAssignableFrom(BufferedSource::class.java) -> {
                response as J
            }
            else -> {
                super.convert(response, requestFields, clazz)
            }
        }
    }

}