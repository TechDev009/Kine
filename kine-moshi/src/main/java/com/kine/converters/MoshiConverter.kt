package com.kine.converters

import com.kine.request.Request
import com.squareup.moshi.Moshi


class MoshiConverter(private var moshi: Moshi = Moshi.Builder().build()) : Converter {
    override fun <J> convert(response: Any, request: Request, clazz: Class<J>): J? {

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