package com.kine.converters.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

fun <J> Gson.fromJsonArray(response: String):ArrayList<J>{
    val listType = object : TypeToken<ArrayList<J>?>() {}.type
    return this.fromJson(response, listType)
}