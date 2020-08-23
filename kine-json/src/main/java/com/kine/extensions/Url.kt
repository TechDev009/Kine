@file:Suppress1("unused")

package com.kine.extensions


import com.kine.KineRequest
import com.kine.converters.JsonConverter
import com.kine.response.KineResponse
import com.kine.response.OnError
import com.kine.response.OnSuccess
import org.json.JSONArray
import org.json.JSONObject
import kotlin.Suppress as Suppress1


fun Map<*,*>.toJsonObject(): JSONObject {
    return JSONObject(this)
}

fun KineRequest.RequestBodyBuilder.bodyParams(jsonObject : JSONObject?): KineRequest.RequestBodyBuilder {
    return bodyParams(jsonObject?.toString())
}
fun KineRequest.RequestBodyBuilder.bodyParams(jsonObject : JSONArray?): KineRequest.RequestBodyBuilder {
    return bodyParams(jsonObject?.toString())
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
fun  KineRequest.RequestOptionsBuilder.responseAsJson(onSuccess: OnSuccess<JSONObject>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONObject::class.java,onSuccess, onError)
}
fun  KineRequest.RequestOptionsBuilder.responseAsJsonArray(onSuccess: OnSuccess<JSONArray>, onError: OnError){
    return this.converter(JsonConverter()).responseAs(JSONArray::class.java,onSuccess, onError)
}
fun  KineRequest.RequestOptionsBuilder.responseAsJson(): KineResponse<JSONObject>? {
    return this.converter(JsonConverter()).responseAs(JSONObject::class.java)
}
fun  KineRequest.RequestOptionsBuilder.responseAsJsonArray(): KineResponse<JSONArray>? {
    return this.converter(JsonConverter()).responseAs(JSONArray::class.java)
}