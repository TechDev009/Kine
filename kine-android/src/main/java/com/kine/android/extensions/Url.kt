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

fun KineRequest.RequestBodyBuilder.bodyParams(jsonObject : JSONObject?): KineRequest.RequestBodyBuilder {
    return bodyParams(jsonObject?.toString())
}
fun KineRequest.RequestBodyBuilder.bodyParams(jsonObject : JSONArray?): KineRequest.RequestBodyBuilder {
    return bodyParams(jsonObject?.toString())
}
fun KineRequest.RequestBodyBuilder.bodyParams(jsonObject : JsonRequestBody): KineRequest.RequestBodyBuilder {
    return bodyParams(jsonObject.body,jsonObject.mediaType)
}
fun String.httpPost(jsonObject : JSONObject?): KineRequest.RequestBodyBuilder {
    return KineRequest.post(this).bodyParams(jsonObject?.toString())
}
fun String.httpDelete(jsonObject : JSONObject?): KineRequest.RequestBodyBuilder {
    return KineRequest.delete(this).bodyParams(jsonObject?.toString())
}
fun String.httpPut(jsonObject : JSONObject?): KineRequest.RequestBodyBuilder {
    return KineRequest.put(this).bodyParams(jsonObject?.toString())
}
fun String.httpPatch(jsonObject : JSONObject?): KineRequest.RequestBodyBuilder {
    return KineRequest.patch(this).bodyParams(jsonObject?.toString())
}
fun <F> String.httpPostAs(jsonObject : JSONObject?, clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError){
    return this.httpPost(jsonObject).responseAs(clazz,onSuccess, onError)
}
fun <F> KineRequest.RequestOptionsBuilder.responseAsJson(onSuccess: OnSuccess<JSONObject>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONObject::class.java,onSuccess, onError)
}
fun <F> KineRequest.RequestOptionsBuilder.responseAsJsonArray(onSuccess: OnSuccess<JSONArray>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONArray::class.java,onSuccess, onError)
}