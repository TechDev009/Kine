@file:JvmName("ResponseListenerKt")

package com.kine.response


typealias OnSuccess<F> = ((KineResponse<F>) -> Unit)

typealias OnError = ((KineError) -> Unit)