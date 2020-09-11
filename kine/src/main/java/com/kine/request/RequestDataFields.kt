package com.kine.request


/**
 * An RequestDataFields. This class is used to hold request param information associated with a single HTTP request.
 */
data class RequestDataFields(val reqTAG: String, var url: String, val method: Int,
                             val body : RequestBody, val headers: HashMap<String, String?>?)