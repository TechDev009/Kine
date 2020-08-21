package com.kine.coroutine

import com.kine.KineRequest
import com.kine.internal.DefaultKineClass
import com.kine.response.KineResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

suspend fun <F> KineRequest.IBuildOptions.responseAsCoroutine(clazz: Class<F>, scope: CoroutineContext = Dispatchers.IO): KineResponse<F>? {
    return build().responseAsCoroutine(clazz, scope)
}

suspend fun <F> KineRequest.responseAsCoroutine(
    clazz: Class<F>,
    scope: CoroutineContext = Dispatchers.IO
): KineResponse<F>? {
    val response: KineResponse<F>?
    withContext(scope) {
        response = execute(DefaultKineClass(clazz))
    }
    return response
}