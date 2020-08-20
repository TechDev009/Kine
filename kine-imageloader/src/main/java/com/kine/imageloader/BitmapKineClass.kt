package com.kine.imageloader

import android.graphics.Bitmap
import com.kine.internal.KineClass

class BitmapKineClass : KineClass<Bitmap>(Bitmap::class.java) {
    override fun isAssignableFrom(t: Class<*>): Boolean {
        return t.isAssignableFrom(ByteArray::class.java) || t.isAssignableFrom(Bitmap::class.java)
    }
}