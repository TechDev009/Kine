package com.kine.converters

import com.kine.request.Request

class SimpleStringConverter:Converter {
    override fun <J> convert(response: Any, request: Request, clazz: Class<J>): J? {
        return if(clazz.isAssignableFrom(String::class.java)) response as J
        else null
    }
}