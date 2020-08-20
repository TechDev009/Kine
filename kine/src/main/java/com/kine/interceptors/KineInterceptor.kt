package com.kine.interceptors

import com.kine.request.Request
import com.kine.response.KineResponse
import kotlin.jvm.Throws

abstract class KineInterceptor {
    @Throws(Throwable::class)
    abstract  fun <T> intercept(request: Request,clazz: Class<T>):KineResponse<T>?

}