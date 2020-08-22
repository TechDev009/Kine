package com.kine.request


/**
 * An Request. This class is used to hold request param information associated with a single HTTP request.
 */
data class RequestData(val reqTAG: String, var url: String, val method: Int,
                       val body : RequestBody, val headers: HashMap<String, String?>?)