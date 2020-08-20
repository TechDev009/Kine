package com.kine.request

import java.util.HashMap

class EncodedRequestBody(bodyParams: HashMap<String, String>?, encodedBodyParams: HashMap<String, String>?)
    :RequestBody(null, ContentType.ENCODED.toString(),bodyParams,encodedBodyParams) {
}