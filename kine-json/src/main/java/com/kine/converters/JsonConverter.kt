@file:Suppress("UNCHECKED_CAST")

package com.kine.converters

import com.kine.request.Request
import org.json.JSONArray
import org.json.JSONObject


class JsonConverter : RawResponseConverter() {
    override fun <J> convert(response: Any, request: Request, clazz: Class<J>): J? {
        if (response is String) {
            return when {
                clazz.isAssignableFrom(String::class.java) -> {
                    response as J
                }
                clazz.isAssignableFrom(JSONObject::class.java) -> {
                    JSONObject(response) as J
                }
                clazz.isAssignableFrom(JSONArray::class.java) -> {
                    JSONArray(response) as J
                }
                else -> {
                    null
                }
            }
        } else {
            return super.convert(response, request, clazz)
        }
    }

}