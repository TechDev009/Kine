package com.kine.extensions

import com.kine.KineRequest
import com.kine.converters.FileDownloadConverter
import com.kine.response.OnError
import com.kine.response.OnSuccess
import java.io.File

/**
 *  extensions function for making a request.
 */
typealias ProgressListener = (Long,Long)->Unit

fun String.httpGet(): KineRequest.IBuildOptions {
    return KineRequest.get().url(this)
}
fun String.httpHead(): KineRequest.IBuildOptions {
    return KineRequest.head().url(this)
}
fun String.httpPost(params : String?=null): KineRequest.IBuildOptions {
    return KineRequest.post(params).url(this)
}
fun String.httpDelete(params : String?=null): KineRequest.IBuildOptions {
    return KineRequest.delete(params).url(this)
}
fun String.httpPut(params : String?=null): KineRequest.IBuildOptions {
    return KineRequest.put(params).url(this)
}
fun String.httpPatch(params : String?=null): KineRequest.IBuildOptions {
    return KineRequest.patch(params).url(this)
}
fun String.httpMethod(method : Int,params : String?=null): KineRequest.IBuildOptions {
    return KineRequest.method(method).url(this).bodyParams(params)
}
fun <F> String.httpGetAs(clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError) {
    return this.httpGet().responseAs(clazz,onSuccess, onError)
}
fun <F> String.httpPostAs(params : String?,clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError){
    return this.httpPost(params).responseAs(clazz,onSuccess, onError)
}
fun <F> KineRequest.IBuildOptions.httpGetAsString(onSuccess: OnSuccess<String>, onError: OnError){
    return responseAs(String::class.java,onSuccess, onError)
}

fun String.formatUrl(vararg values:Any): String {
    return String.format(this,values)
}

fun File.downloadFrom(url:String, progressListener: ProgressListener,onSuccess: OnSuccess<File>?=null, onError: OnError?=null) {
    url.httpGet().converter(FileDownloadConverter()).downloadFile(this,progressListener, { response ->
         onSuccess?.invoke(response)
        }, onError = {kineError ->
        kineError.exception.printStackTrace()
        onError?.invoke(kineError)
        })
}
fun String.downloadTo(file:File, progressListener: ProgressListener,onSuccess: OnSuccess<File>?=null, onError: OnError?=null) {
    file.downloadFrom(this,progressListener, onSuccess, onError)
}


