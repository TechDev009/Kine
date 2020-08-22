package com.kine.rxjava3

import com.kine.KineRequest
import com.kine.internal.DefaultKineClass
import com.kine.response.KineResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

fun <F> KineRequest.RequestOptionsBuilder.toSingle(callback:Class<F>): Single<KineResponse<F>?> {
    return build().toSingle(callback)
}
fun <F> KineRequest.toSingle(callback:Class<F>): Single<KineResponse<F>?> {
    return Single.fromCallable {
        this.execute(DefaultKineClass(callback))
    }
}
fun <F> KineRequest.RequestOptionsBuilder.toFlowable(callback:Class<F>): Flowable<KineResponse<F>?> {
    return build().toFlowable(callback)
}
fun <F> KineRequest.toFlowable(callback:Class<F>): Flowable<KineResponse<F>?> {
    return Flowable.fromCallable {
        this.execute(DefaultKineClass(callback))
    }
}
fun <F> KineRequest.RequestOptionsBuilder.toObservable(callback:Class<F>): Observable<KineResponse<F>?> {
    return build().toObservable(callback)
}
fun <F> KineRequest.toObservable(callback:Class<F>): Observable<KineResponse<F>?> {
    return Observable.fromCallable {
        this.execute(DefaultKineClass(callback))
    }
}
