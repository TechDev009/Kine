@file:Suppress("UNCHECKED_CAST")

package com.kine.android.converters

import com.kine.converters.Converter
import com.kine.request.Request
import org.json.JSONArray
import org.json.JSONObject


class JsonConverter : Converter {
    override fun <J> convert(response: Any,request:Request, clazz: Class<J>): J? {
        return when (response is String) {
            clazz.isAssignableFrom(String::class.java) -> {
                response as J
            }
            clazz.isAssignableFrom(JSONObject::class.java) -> {
                JSONObject(response as String) as J
            }
            clazz.isAssignableFrom(JSONArray::class.java) -> {
                JSONArray(response as String) as J
            }
            else -> {
                null
            }
        }
    }

}