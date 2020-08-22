package com.kine.extensions

import java.net.URLConnection

fun String.getMimeType(): String {
    return URLConnection.getFileNameMap().getContentTypeFor(this)?:"application/octet-stream"
}