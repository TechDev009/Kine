package com.kine.request

import java.io.File
import java.util.*

class MultiPartRequestBody(
    var multiPartParams: HashMap<String, MultiPartStringBody> = hashMapOf(),
    var multiPartFileParams: HashMap<String, MutableList<MultiPartFileBody>> = hashMapOf()
) : RequestBody() {

    fun addMultiPartFileParam(key: String, value: File, contentType: String? = null) {
        multiPartFileParams[key] =  multiPartFileParams[key]?.apply {add(
            MultiPartFileBody(
                value,
                contentType
            )
        )}
            ?:arrayListOf(MultiPartFileBody(value, contentType))
    }

    fun addMultiPartParam(key: String, value: String, contentType: String? = null) {
        multiPartParams[key] = MultiPartStringBody(value, contentType)
    }
    fun addMultiPartFileParams(parts: HashMap<String, File>, contentType: String? = null) {

        for ((key, file) in parts.entries) {
            val fileBodies: MutableList<MultiPartFileBody>? = multiPartFileParams[key]?:ArrayList<MultiPartFileBody>()
            fileBodies!!.add(MultiPartFileBody(file, contentType))
            multiPartFileParams[key] = fileBodies
        }
    }
    fun addMultiPartFileListParams(parts: HashMap<String, List<File>>, contentType: String? = null) {
        for ((key, fileList) in parts.entries) {
            val fileBodies: MutableList<MultiPartFileBody> = multiPartFileParams[key] ?: ArrayList<MultiPartFileBody>()
            for (file in fileList) {
                fileBodies.add(MultiPartFileBody(file, contentType))
            }
            multiPartFileParams[key] = fileBodies
        }
    }
    fun addMultiPartParams(parts: HashMap<String, String>, contentType: String? = null) {
        parts.apply {
            for ((key, stringBody) in this.entries) {
                multiPartParams[key] = MultiPartStringBody(stringBody, contentType)
            }
        }
    }
}