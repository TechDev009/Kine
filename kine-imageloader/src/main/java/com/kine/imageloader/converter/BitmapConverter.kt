@file:Suppress("UNCHECKED_CAST")

package com.kine.imageloader.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.kine.converters.Converter
import com.kine.request.Request

class BitmapConverter :Converter{
    override fun <J> convert(response: Any, request: Request, clazz: Class<J>): J? {
            if(clazz.isAssignableFrom(Bitmap::class.java)){
                return when (response) {
                    is ByteArray -> {
                        BitmapFactory.decodeByteArray(response, 0, response.size) as J
                    }
                    else -> {
                        null
                    }
                }
            }
        return null
    }

}