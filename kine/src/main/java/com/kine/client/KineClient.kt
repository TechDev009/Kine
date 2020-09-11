package com.kine.client

import com.kine.request.RequestFields
import com.kine.response.KineResponse

/**
 * kineClient for making the actual HTTP request to server.
 */
abstract class KineClient constructor(){

    abstract fun canHandleRequest(url: String, method: Int): Boolean

    @Throws(Throwable::class)
    abstract fun <T> execute(requestFields: RequestFields, clazz: Class<T>): KineResponse<T>

    abstract fun cancelAllRequests(tag: String?=null)

}