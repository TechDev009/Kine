@file:JvmName("ImageLoader")

package com.kine.imageloader.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.kine.KineRequest
import com.kine.exceptions.NullResponseException
import com.kine.extensions.httpGet
import com.kine.imageloader.BitmapKineClass
import com.kine.imageloader.converter.BitmapConverter
import com.kine.response.KineError
import com.kine.response.KineResponse
import com.kine.response.OnError
import com.kine.response.OnSuccess

inline fun ImageView.loadImage(url: String, @DrawableRes placeHolder: Int? = null,crossinline onSuccess: OnSuccess<Bitmap>,
                        crossinline onError: OnError = { _ -> }) {
    placeHolder?.let {
        setImageResource(it)
    }
    url.httpGet().converter(BitmapConverter())
        .responseAs(BitmapKineClass(), { response ->
            setImageBitmap(response.body)
            onSuccess.invoke(response)
        }, onError = { error ->
            error.printStackTrace()
            onError.invoke(error)
        })
}

fun ImageView.loadImage(url: String, @DrawableRes placeHolder: Int? = null) {
    placeHolder?.let {
        setImageResource(it)
    }
    val response = url.httpGet().converter(BitmapConverter())
        .responseAs(BitmapKineClass())
    if (response?.body != null) {
        setImageBitmap(response.body)
    }
}

inline fun String.loadBitmapFromUrl(
    crossinline onSuccess: (Bitmap) -> Unit,
    crossinline onError: OnError = { _ -> }
) {
    this.httpGet().converter(BitmapConverter())
        .responseAs(BitmapKineClass(), { response ->
            response.body?:onError(KineError(NullResponseException("null bitmap")))
            response.body?.apply {
                onSuccess(this)
            }
        }, onError = { error ->
            error.printStackTrace()
            onError(error)
        })
}

inline fun String.loadBitmapResponseFromUrl(crossinline onSuccess: OnSuccess<Bitmap>, crossinline onError: OnError = { _ -> }
) {
    this.httpGet().converter(BitmapConverter())
        .responseAs(BitmapKineClass(), { response ->
            onSuccess(response)
        }, onError = { error ->
            error.printStackTrace()
            onError(error)
        })
}

inline fun KineRequest.RequestOptionsBuilder.loadBitmap(crossinline onSuccess: OnSuccess<Bitmap>, crossinline onError: OnError = { _ -> }) {
    return converter(BitmapConverter()).responseAs(BitmapKineClass(), { response ->
        response.body?:onError(KineError(NullResponseException("null bitmap")))
        response.body?.apply {
            onSuccess(response)
        }
    }, onError = { error ->
        error.printStackTrace()
        onError(error)
    })
}
 fun KineRequest.RequestOptionsBuilder.loadBitmap(): KineResponse<Bitmap>? {
    return converter(BitmapConverter()).responseAs(BitmapKineClass())
}
