package com.kine.request

/**
 * Helper class for content-type(media-type).
 */

enum class ContentType(private val value:String) {
    JSON("application/json; charset=utf-8"), STRING("text/plain; charset=utf-8"),
    ENCODED("application/x-www-form-urlencoded");

    override fun toString(): String {
        return value
    }
}