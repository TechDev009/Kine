package com.kine.converters

import com.kine.request.RequestFields
import java.io.File


class FileDownloadConverter : Converter {
    @Suppress("UNCHECKED_CAST")
    override fun <J> convert(response: Any, requestFields: RequestFields, clazz: Class<J>): J? {
        if (clazz.isAssignableFrom(File::class.java) && response is File) {
            return response as J
        }
        return null
    }

}