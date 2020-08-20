package com.kine.converters

import com.google.gson.Gson
import com.kine.converters.Converter
import com.kine.request.Request

class GsonConverter(private var gson: Gson = Gson()) : Converter {
    override fun <J> convert(response: Any,request: Request, clazz: Class<J>): J? {
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