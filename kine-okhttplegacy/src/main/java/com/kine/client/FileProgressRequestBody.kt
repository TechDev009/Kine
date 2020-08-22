package com.kine.client


import com.kine.extensions.ProgressListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

internal class FileProgressRequestBody(
    private val requestBody: RequestBody,
    private val progressListener: ProgressListener?
) : RequestBody() {

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (progressListener == null) {
            requestBody.writeTo(sink)
            return
        }
        val progressSink = Okio.buffer(wrapSink(sink))
        requestBody.writeTo(progressSink)
        progressSink.flush()
    }

    private fun wrapSink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            var bytesWritten = 0L
            var contentLength = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    contentLength = contentLength()
                }
                bytesWritten += byteCount
                progressListener?.invoke(bytesWritten, contentLength)

            }
        }
    }
}