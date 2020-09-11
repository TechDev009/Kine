package com.kine.converters

import com.google.gson.Gson
import com.kine.request.RequestFields

class GsonConverter(private var gson: Gson = Gson()) : Converter {
    override fun <J> convert(response: Any, requestFields: RequestFields, clazz: Class<J>): J? {
        return when (response) {
            is String -> {
                gson.fromJson(response, clazz)
            }
            else -> {
                null
            }
        }
    }

}