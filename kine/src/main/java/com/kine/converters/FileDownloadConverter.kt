package com.kine.converters

import com.kine.request.Request
import java.io.File


class FileDownloadConverter : Converter {
    override fun <J> convert(response: Any, request: Request, clazz: Class<J>): J? {
        if (clazz.isAssignableFrom(File::class.java) && response is File) {
            return response as J
        }
        return null
    }

}