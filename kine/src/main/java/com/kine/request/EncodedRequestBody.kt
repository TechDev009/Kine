package com.kine.request

import java.util.*

class EncodedRequestBody(var bodyParams: HashMap<String, String> = hashMapOf(), var encodedBodyParams: HashMap<String, String> = hashMapOf())
    :RequestBody(ContentType.ENCODED.toString()) {


    fun setBody(bodyParams: HashMap<String, String>? = null) {

        this.mediaType = ContentType.ENCODED.toString()
        this.bodyParams = bodyParams?: hashMapOf()
    }
    fun setBodyEncoded(encodedBodyParams: HashMap<String, String>? = null) {

        this.mediaType = ContentType.ENCODED.toString()
        this.encodedBodyParams = encodedBodyParams?: hashMapOf()
    }
    fun setBody(bodyParams: HashMap<String, String>? = null,
                encodedBodyParams: HashMap<String, String>? = null) {

        this.mediaType = ContentType.ENCODED.toString()
        this.bodyParams = bodyParams?: hashMapOf()
        this.encodedBodyParams = encodedBodyParams?: hashMapOf()
    }

    fun addBodyParam(key: String, value: String) {
        bodyParams[key] = value
    }
    fun addBodyParamEncoded(key: String, value: String) {
        encodedBodyParams[key] = value
    }
}