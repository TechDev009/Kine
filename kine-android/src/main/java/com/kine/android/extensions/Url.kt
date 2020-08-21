@file:Suppress1("unused")

package com.kine.android.extensions


import android.net.Uri
import com.kine.KineRequest
import com.kine.android.converters.JsonConverter
import com.kine.android.request.JsonRequestBody
import com.kine.response.OnError
import com.kine.response.OnSuccess
import org.json.JSONArray
import org.json.JSONObject
import kotlin.Suppress as Suppress1

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
fun KineRequest.httpPost(jsonObject : JSONObject?): KineRequest.IBuildUrl {
    return KineRequest.post(jsonObject?.toString())
}
fun KineRequest.httpDelete(jsonObject : JSONObject?): KineRequest.IBuildUrl {
    return KineRequest.delete(jsonObject?.toString())
}
fun KineRequest.httpPut(jsonObject : JSONObject?): KineRequest.IBuildUrl {
    return KineRequest.put(jsonObject?.toString())
}
fun KineRequest.httpPatch(jsonObject : JSONObject?): KineRequest.IBuildUrl {
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
fun String.httpPost(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.post(jsonObject?.toString()).url(this)
}
fun String.httpDelete(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.delete(jsonObject?.toString()).url(this)
}
fun String.httpPut(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.put(jsonObject?.toString()).url(this)
}
fun String.httpPatch(jsonObject : JSONObject?): KineRequest.IBuildOptions {
    return KineRequest.patch(jsonObject?.toString()).url(this)
}
fun <F> String.httpPostAs(jsonObject : JSONObject?, clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError){
    return this.httpPost(jsonObject).responseAs(clazz,onSuccess, onError)
}

fun <F> KineRequest.IBuildOptions.responseAsJson(onSuccess: OnSuccess<JSONObject>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONObject::class.java,onSuccess, onError)
}
fun <F> KineRequest.IBuildOptions.responseAsJsonArray(onSuccess: OnSuccess<JSONArray>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONArray::class.java,onSuccess, onError)
}