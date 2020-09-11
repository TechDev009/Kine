package com.kine.interceptors

import com.kine.request.RequestFields
import com.kine.response.KineResponse
import kotlin.jvm.Throws

abstract class KineInterceptor {
    @Throws(Throwable::class)
    abstract  fun <T> intercept(requestFields: RequestFields, clazz: Class<T>):KineResponse<T>?

}