package com.kine.converters.extensions


import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

fun <J> Moshi.fromJsonArray(response: String,clazz: Class<J>): List<J>? {
    val type: Type = com.squareup.moshi.Types.newParameterizedType(MutableList::class.java, clazz)
    val adapter: JsonAdapter<List<J>> = this.adapter(type)
    return adapter.fromJson(response)
}