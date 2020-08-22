package com.kine.extensions

import com.kine.KineRequest
import com.kine.request.ContentType
import com.kine.response.KineResponse
import com.kine.response.OnError
import com.kine.response.OnSuccess
import java.io.File

/**
 *  extensions function for making a request.
 */
typealias ProgressListener = (Long,Long)->Unit

fun String.httpGet(): KineRequest.RequestOptionsBuilder {
    return KineRequest.get(this)
}
fun String.httpHead(): KineRequest.RequestOptionsBuilder {
    return KineRequest.head(this)
}
fun String.httpPost(params : String?=null,contentType: String=ContentType.JSON.toString()): KineRequest.RequestBodyBuilder {
    return KineRequest.post(this).bodyParams(params,contentType)
}
fun String.httpDelete(params : String?=null,contentType: String=ContentType.JSON.toString()): KineRequest.RequestBodyBuilder {
    return KineRequest.delete(this).bodyParams(params,contentType)
}
fun String.httpPut(params : String?=null,contentType: String= ContentType.JSON.toString()): KineRequest.RequestBodyBuilder {
    return KineRequest.put(this).bodyParams(params,contentType)
}
fun String.httpPatch(params : String?=null,contentType: String=ContentType.JSON.toString()): KineRequest.RequestBodyBuilder {
    return KineRequest.patch(this).bodyParams(params,contentType)
}
fun String.httpMethod(method : Int): KineRequest.RequestOptionsBuilder {
    return KineRequest.method(this,method)
}
fun <F> String.httpGetAs(clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError) {
    return this.httpGet().responseAs(clazz,onSuccess, onError)
}
fun <F> String.httpGetAs(clazz: Class<F>): KineResponse<F>? {
    return this.httpGet().responseAs(clazz)
}
fun <F> String.httpPostAs(params : String?,clazz: Class<F>): KineResponse<F>? {
    return this.httpPost(params).responseAs(clazz)
}
fun <F> String.httpPostAs(params : String?,clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError){
    return this.httpPost(params).responseAs(clazz,onSuccess, onError)
}
fun <F> KineRequest.RequestOptionsBuilder.httpGetAsString(onSuccess: OnSuccess<String>, onError: OnError){
    return responseAs(String::class.java,onSuccess, onError)
}

fun String.formatUrl(vararg values:Any): String {
    return String.format(this,values)
}

fun File.downloadFrom(url:String, progressListener: ProgressListener,onSuccess: OnSuccess<File>?=null, onError: OnError?=null) {
    url.httpGet().downloadFile(this,progressListener, { response ->
         onSuccess?.invoke(response)
        }, onError = {kineError ->
        kineError.exception.printStackTrace()
        onError?.invoke(kineError)
        })
}
fun String.downloadTo(file:File, progressListener: ProgressListener,onSuccess: OnSuccess<File>?=null, onError: OnError?=null) {
    file.downloadFrom(this,progressListener, onSuccess, onError)
}


