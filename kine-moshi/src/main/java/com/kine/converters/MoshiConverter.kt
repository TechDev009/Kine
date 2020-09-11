package com.kine.converters

import com.kine.request.RequestFields
import com.squareup.moshi.Moshi


class MoshiConverter(private var moshi: Moshi = Moshi.Builder().build()) : Converter {
    override fun <J> convert(response: Any, requestFields: RequestFields, clazz: Class<J>): J? {

        return when (response) {
            is String -> {
                moshi.adapter(clazz).fromJson(response)
            }
            else -> {
                null
            }
        }
    }

}