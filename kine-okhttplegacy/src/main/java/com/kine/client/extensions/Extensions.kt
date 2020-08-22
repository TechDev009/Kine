package com.kine.client.extensions


import com.kine.KineRequest
import com.kine.client.OkHttpKineClient
import com.kine.extensions.ProgressListener
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okio.*
import okio.Okio.sink
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream

fun String.buildUrl(
    pathParams: List<String>? = null,
    queryParams: HashMap<String, String>? = null,
    encodedPathParams: List<String>? = null,
    encodedQueryParams: HashMap<String, String>? = null
): String {
    val urlBuilder = HttpUrl.parse(this)?.newBuilder() ?: throw IllegalArgumentException("not a valid url")
    pathParams?.apply {
        for (value in this) {
            urlBuilder.addEncodedPathSegment(value)
        }
    }
    encodedPathParams?.apply {
        for (value in this) {
            urlBuilder.addEncodedPathSegment(value)
        }
    }
    queryParams?.apply {
        val entries: Set<Map.Entry<String, String>> = this.entries
        for ((name, value) in entries) {
            urlBuilder.addQueryParameter(name, value)
        }
    }
    encodedQueryParams?.apply {
        val entries: Set<Map.Entry<String, String>> = this.entries
        for ((name, value) in entries) {
            urlBuilder.addEncodedQueryParameter(name, value)
        }
    }
    return urlBuilder.build().toString()
}

const val DOWNLOAD_CHUNK_SIZE = 2048L
internal inline fun File.download(
    body: ResponseBody,
    crossinline progressListener: ProgressListener
): File {
    val contentLength: Long = body.contentLength()
    val source: BufferedSource = body.source()
    val sink: BufferedSink = Okio.buffer(sink(this))
    sink.use {
        var totalRead: Long = 0
        var read: Long
        val buffer = Buffer()
        while (source.read(buffer, DOWNLOAD_CHUNK_SIZE).also { read = it } != -1L) {
            sink.write(buffer, read)
            totalRead += read
            progressListener(totalRead, contentLength)
        }
        sink.flush()
        sink.close()
        source.close()
        progressListener(contentLength, contentLength)
    }
    return this
}

internal inline fun FileDescriptor.download(
    body: ResponseBody,
    crossinline progressListener: ProgressListener
) {
    FileOutputStream(this).download(body, progressListener)
}

internal inline fun FileOutputStream.download(
    body: ResponseBody,
    crossinline progressListener: ProgressListener
) {
    use {
        val contentLength: Long = body.contentLength()
        val source: BufferedSource = body.source()
        val sink: BufferedSink = Okio.buffer(sink(this))
        var totalRead: Long = 0
        var read: Long
        val buffer = Buffer()
        while (source.read(buffer, DOWNLOAD_CHUNK_SIZE).also { read = it } != -1L) {
            sink.write(buffer, read)
            totalRead += read
            progressListener(totalRead, contentLength)
        }
        sink.flush()
        sink.close()
        source.close()
        close()
        progressListener(contentLength, contentLength)
    }
}
fun KineRequest.RequestOptionsBuilder.client(okHttpClient: OkHttpClient): KineRequest.RequestOptionsBuilder {
    return client(okHttpClient.toKineClient())
}
fun OkHttpClient.toKineClient(): OkHttpKineClient {
    return OkHttpKineClient(this)
}




