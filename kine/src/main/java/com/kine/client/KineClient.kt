package com.kine.client

import com.kine.request.Request
import com.kine.response.KineResponse

/**
 * kineClient for making the actual HTTP request to server.
 */
abstract class KineClient constructor(){

    abstract fun canHandleRequest(url: String, method: Int): Boolean

    @Throws(Throwable::class)
    abstract fun <T> execute(request: Request, clazz: Class<T>): KineResponse<T>

    abstract fun cancelAllRequests(tag: String?=null)

}