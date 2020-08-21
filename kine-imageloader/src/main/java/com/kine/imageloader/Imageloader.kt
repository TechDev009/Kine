package com.kine.imageloader

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.kine.exceptions.NullResponseException
import com.kine.extensions.httpGet
import com.kine.imageloader.converter.BitmapConverter
import com.kine.response.KineError
import com.kine.response.KineResponse
import com.kine.response.OnError

fun ImageView.loadImage(url:String,@DrawableRes placeHolder:Int?=null){
    placeHolder?.let {
        setImageResource(it)
    }
    url.httpGet().converter(BitmapConverter())
        .responseAs(BitmapKineClass(), { response ->
            setImageBitmap(response.response)
        }, onError = {error ->
            error.printStackTrace()
        })
}
inline fun String.loadBitmapFromUrl(crossinline onSuccess: (Bitmap)->Unit,crossinline onError: OnError={ _ ->}){
    this.httpGet().converter(BitmapConverter())
        .responseAs(BitmapKineClass(), { response ->
            response.response?.apply {
                onSuccess(this)
            }?: onError(KineError( NullResponseException("null bitmap")))
        }, onError = {error->
            error.printStackTrace()
            onError(error)
        })
}
inline fun String.loadBitmapAndResponseFromUrl(crossinline onSuccess: (KineResponse<Bitmap>)->Unit, crossinline onError: OnError={_ ->}){
    this.httpGet().converter(BitmapConverter())
        .responseAs(BitmapKineClass(), { response ->
                onSuccess(response)
        }, onError = {error ->
            error.printStackTrace()
            onError(error)
        })
}