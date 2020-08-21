package com.kine.converters.extensions


import com.squareup.moshi.Moshi
import java.lang.reflect.Type

inline fun <reified J> Moshi.fromJsonArray(response: String,clazz: Class<J>): List<J>? {
    val type: Type = com.squareup.moshi.Types.newParameterizedType(MutableList::class.java, clazz)
    return this.adapter<List<J>>(type).fromJson(response)
}