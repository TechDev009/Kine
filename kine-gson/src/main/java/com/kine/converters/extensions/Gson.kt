package com.kine.converters.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

inline fun <reified J> Gson.fromJsonArray(response: String):ArrayList<J>{
    val listType = object : TypeToken<ArrayList<J>?>() {}.type
    return this.fromJson(response, listType)
}
fun <J> Gson.getArrayType(): Type {
    return object : TypeToken<ArrayList<J>>() {}.type
}
fun <J> J.getGsonArrayType(): Type {
    return object : TypeToken<ArrayList<J>>() {}.type
}