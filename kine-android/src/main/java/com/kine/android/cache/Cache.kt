package com.kine.android.cache

import com.kine.response.KineResponse

/**
 * interface for caching response.
 */
interface Cache {
    operator fun get(key: String): CacheEntry?
    fun remove(key: String)
    fun clear()
    fun put(key: String, cacheEntry: CacheEntry?)

    class CacheEntry(kineResponse: KineResponse<String?>, cacheTime: Long, reqTAG: String, timeStampMillis: Long) {

        val cacheDuration: Long

        val url: String

        val timeStampMillis: Long

        /**
         * data from this kineResponse.
         */
        val data: String?

        /**
         * The HTTP status code.
         */
        @JvmField
        val statusCode: Int

        /**
         * KineResponse headers.
         */
        val headers: Map<String, String>

        /**
         * Network roundtrip time in milliseconds.
         */
        @JvmField
        val networkTimeMs: Long

        /**
         * Network roundtrip time in milliseconds.
         */
        val loadedFrom: Int

        init {
            data = kineResponse.response
            headers = kineResponse.headers
            networkTimeMs = kineResponse.networkTimeMs
            loadedFrom = kineResponse.loadedFrom
            statusCode = kineResponse.statusCode
            cacheDuration = cacheTime
            url = reqTAG
            this.timeStampMillis = timeStampMillis
        }
    }
}