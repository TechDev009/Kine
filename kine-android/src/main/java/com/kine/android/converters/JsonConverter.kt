@file:Suppress("UNCHECKED_CAST")

package com.kine.android.converters

import com.kine.converters.RawResponseConverter
import com.kine.request.RequestFields
import org.json.JSONArray
import org.json.JSONObject


class JsonConverter : RawResponseConverter() {
    override fun <J> convert(response: Any, requestFields: RequestFields, clazz: Class<J>): J? {
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
            return super.convert(response, requestFields, clazz)
        }
    }

}