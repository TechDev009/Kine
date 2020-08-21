package com.kine.android.extensions

import android.net.Uri
import com.kine.KineRequest
import com.kine.android.converters.JsonConverter
import com.kine.android.request.JsonRequestBody
import com.kine.response.OnError
import com.kine.response.OnSuccess
import org.json.JSONArray
import org.json.JSONObject

fun String.buildUrl(pathParams:List<String>?=null,queryParams:HashMap<String,String>?=null,
                    encodedPathParams:List<String>?=null): String {
    val builder =  Uri.parse(this)
        .buildUpon()
    pathParams?.apply {
        for (value in this) {
            builder.appendPath(value)
        }
    }
    encodedPathParams?.apply {
        for (value in this) {
            builder.appendEncodedPath(value)
        }
    }
    queryParams?.apply {
        val entries: Set<Map.Entry<String, String>> = this.entries
        for ((name, value) in entries) {
            builder.appendQueryParameter(name, value)
        }
    }
    return builder.build().toString()
}
fun Map<*,*>.toJsonObject(): JSONObject {
    return JSONObject(this)
}
fun KineRequest.post(jsonObject : JSONObject?): KineRequest.IBuildUrl {
    return KineRequest.post(jsonObject?.toString())
}
fun KineRequest.delete(jsonObject : JSONObject?): KineRequest.IBuildUrl {
    return KineRequest.delete(jsonObject?.toString())
}
fun KineRequest.put(jsonObject : JSONObject?): KineRequest.IBuildUrl {
    return KineRequest.put(jsonObject?.toString())
}
fun KineRequest.patch(jsonObject : JSONObject?): KineRequest.IBuildUrl {
    return KineRequest.patch(jsonObject?.toString())
}
fun KineRequest.IBuildOptions.bodyParams(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return bodyParams(jsonObject?.toString())
}
fun KineRequest.IBuildOptions.bodyParams(jsonObject : JSONArray?): KineRequest.IBuildOptions {
    return bodyParams(jsonObject?.toString())
}
fun KineRequest.IBuildOptions.bodyParams(jsonObject : JsonRequestBody): KineRequest.IBuildOptions {
    return bodyParams(jsonObject.body,jsonObject.mediaType)
}
fun String.post(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.post(jsonObject?.toString()).url(this)
}
fun String.delete(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.delete(jsonObject?.toString()).url(this)
}
fun String.put(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.put(jsonObject?.toString()).url(this)
}
fun String.patch(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.patch(jsonObject?.toString()).url(this)
}
fun <F> String.postAs(jsonObject : JSONObject?,clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError){
    return this.post(jsonObject).responseAs(clazz,onSuccess, onError)
}

fun <F> KineRequest.IBuildOptions.getAsJson(onSuccess: OnSuccess<JSONObject>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONObject::class.java,onSuccess, onError)
}
fun <F> KineRequest.IBuildOptions.getAsJsonArray(onSuccess: OnSuccess<JSONArray>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONArray::class.java,onSuccess, onError)
}