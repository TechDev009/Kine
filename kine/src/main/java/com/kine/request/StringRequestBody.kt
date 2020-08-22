package com.kine.request

open class StringRequestBody(var body:String?=null, mediaType:String = ContentType.JSON.toString()):RequestBody(mediaType) {

    fun setBody(body: String? = null, mediaType: String = ContentType.JSON.toString()) {

        this.body = body
        this.mediaType = mediaType
    }

}