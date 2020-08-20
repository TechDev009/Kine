package com.kine.coroutine

import com.kine.KineRequest
import com.kine.internal.DefaultKineClass
import com.kine.response.KineResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

suspend fun <F> KineRequest.IBuildOptions.getAsCoroutine(clazz: Class<F>, scope: CoroutineContext = Dispatchers.IO): KineResponse<F>? {
    return build().getAsCoroutine(clazz, scope)
}

suspend fun <F> KineRequest.getAsCoroutine(
    clazz: Class<F>,
    scope: CoroutineContext = Dispatchers.IO
): KineResponse<F>? {
    val response: KineResponse<F>?
    withContext(scope) {
        response = executeRequest(DefaultKineClass(clazz))
    }
    return response
}