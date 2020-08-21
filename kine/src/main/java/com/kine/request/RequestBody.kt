package com.kine.request

import java.lang.IllegalArgumentException
import java.util.*


/**
 * This class is used to pass around requestBody information associated with a single HTTP request.
 *
 */
open class RequestBody(var body:String?=null, var mediaType:String = ContentType.JSON.toString(),
                       var bodyParams: HashMap<String, String>? = null,
                       var encodedBodyParams: HashMap<String, String>? = null) {

    fun setBody(body:String?=null, mediaType:String = ContentType.JSON.toString()) {
        if(bodyParams!=null || encodedBodyParams!=null){
            throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
        }
        this.body = body
        this.mediaType = mediaType
    }
    fun setContentType(mediaType:String = ContentType.JSON.toString()) {
        if(bodyParams!=null || encodedBodyParams!=null){
            throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
        }
        this.mediaType = mediaType
    }
    fun setBody(bodyParams: HashMap<String, String>? = null) {
        if(body!=null){
            throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
        }
        this.mediaType = ContentType.ENCODED.toString()
        this.bodyParams = bodyParams
    }
    fun setBodyEncoded(encodedBodyParams: HashMap<String, String>? = null) {
        if(body!=null){
            throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
        }
        this.mediaType = ContentType.ENCODED.toString()
        this.encodedBodyParams = encodedBodyParams
    }
    fun setBody(bodyParams: HashMap<String, String>? = null,
                 encodedBodyParams: HashMap<String, String>? = null) {
        if(body!=null){
            throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
        }
        this.mediaType = ContentType.ENCODED.toString()
        this.bodyParams = bodyParams
        this.encodedBodyParams = encodedBodyParams
    }

    fun addBodyParam(key: String, value: String) {
        if(body!=null){
            throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
        }
        bodyParams = bodyParams ?: hashMapOf()
        bodyParams!![key] = value
    }
    fun addBodyParamEncoded(key: String, value: String) {
        if(body!=null){
            throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
        }
        encodedBodyParams = encodedBodyParams ?: hashMapOf()
        encodedBodyParams!![key] = value
    }
    constructor(bodyParameterMap: HashMap<String, String>?,
                encodedBodyParameterMap: HashMap<String, String>?):
            this(null, ContentType.ENCODED.toString(),bodyParameterMap,encodedBodyParameterMap)

}