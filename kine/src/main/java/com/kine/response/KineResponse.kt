package com.kine.response

import java.util.*

/**
 * This class is used to store the response from the server.
 */
class KineResponse<T> {
    /**  data from this response.  */
    @JvmField
    val response: T?

    /** The HTTP status code.  */
    @JvmField
    val statusCode: Int

    /** KineResponse headers.  */
    @JvmField
    val headers: Map<String, String>

    /** Network roundtrip time in milliseconds.  */
    @JvmField
    val networkTimeMs: Long

    /** source from which response is loaded from.  */
    @JvmField
    val loadedFrom: Int

    /** Parse time in milliseconds.  */
    @JvmField
    var parseTime: Long

    constructor(
        response: T,
        headers: Map<String, String>,
        statusCode: Int,
        networkTimeMs: Long,
        loadedFrom: Int
    ) {
        this.response = response
        this.headers = headers
        this.statusCode = statusCode
        this.networkTimeMs = networkTimeMs
        this.loadedFrom = loadedFrom
        parseTime = 0
    }

    constructor(response: T, loadedFrom: Int) {
        this.response = response
        headers = HashMap()
        statusCode = 200
        networkTimeMs = 0
        this.loadedFrom = loadedFrom
        parseTime = 0
    }

    constructor(loadedFrom: Int) {
        response = null
        headers = HashMap()
        statusCode = 200
        networkTimeMs = 0
        this.loadedFrom = loadedFrom
        parseTime = 0
    }
     fun <T> map(t:T):KineResponse<T>{
        return KineResponse(t,headers,statusCode,networkTimeMs,loadedFrom).apply { parseTime = this@KineResponse.parseTime }
    }
    object LoadedFrom {
        const val DISK = 1
        const val NETWORK = 2
    }
}