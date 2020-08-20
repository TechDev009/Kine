package com.kine.request

import java.util.*


/**
 * This class is used to pass around requestBody information associated with a single HTTP request.
 *
 */
open class RequestBody(var body:String?=null, var mediaType:String = ContentType.JSON.toString(),
                       var bodyParams: HashMap<String, String>? = null,
                       var encodedBodyParams: HashMap<String, String>? = null) {

    constructor(bodyParameterMap: HashMap<String, String>?,
                encodedBodyParameterMap: HashMap<String, String>?):
            this(null, ContentType.ENCODED.toString(),bodyParameterMap,encodedBodyParameterMap)

}