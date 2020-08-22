package com.kine.request


/**
 * This class is used to hold requestBody information associated with a single HTTP request.
 *
 */
abstract class RequestBody(var mediaType: String = ContentType.JSON.toString()) {


    fun setContentType(mediaType: String = ContentType.JSON.toString()) {
        /* if(bodyParams!=null || encodedBodyParams!=null){
             throw IllegalArgumentException("Only one type of params is allowed either params String with contentType or hashMap")
         }*/
        this.mediaType = mediaType
    }

   /* abstract fun <T> toRequestBody(): T*/

}