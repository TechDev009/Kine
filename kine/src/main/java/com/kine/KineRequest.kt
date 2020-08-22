package com.kine


import com.kine.cache.KineCacheControl
import com.kine.client.KineClient
import com.kine.converters.Converter
import com.kine.converters.FileDownloadConverter
import com.kine.exceptions.NoClientFoundException
import com.kine.exceptions.NoConverterFoundException
import com.kine.exceptions.NoInternetException
import com.kine.exceptions.NullResponseException
import com.kine.executor.KineExecutorManager
import com.kine.executor.PriorityRunnable
import com.kine.extensions.ProgressListener
import com.kine.extensions.onCallbackThread
import com.kine.internal.DefaultKineClass
import com.kine.internal.KineClass
import com.kine.internal.RequestManager
import com.kine.log.LogLevel
import com.kine.log.Logger
import com.kine.policies.Priority
import com.kine.policies.RetryPolicy
import com.kine.request.*
import com.kine.response.KineError
import com.kine.response.KineResponse
import com.kine.response.OnError
import com.kine.response.OnSuccess
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 *  Class for making a single HTTP request with config.
 */

open class KineRequest private constructor(requestBuilder: RequestBuilder) {
    private var method = Method.POST
    private val requestUrl: String
    private val requestBody: RequestBody?
    private val kineClient: KineClient?
    private val converter: Converter?
    private val retryPolicy: RetryPolicy?
    private val priority: Priority
    private var reqTAG: String?
    private var networkPolicy = 0
    private val cacheMaxAge: Int
    private var timeUnit: TimeUnit = TimeUnit.SECONDS
    private val headers: HashMap<String, String?>?
    private val queryParams: HashMap<String, String?>?
    private var executor: Executor = KineExecutorManager.executorSupplier.forNetworkTasks()
    private var logLevel = LogLevel.NO_LEVEL
    private var file: File? = null
    private var progressListener: ProgressListener? = null

    init {
        method = requestBuilder.method
        requestUrl = requestBuilder.url
        requestBody = requestBuilder.requestBody
        retryPolicy = requestBuilder.retryPolicy
        reqTAG = requestBuilder.reqTAG
        networkPolicy = requestBuilder.networkPolicy
        priority = requestBuilder.priority
        cacheMaxAge = requestBuilder.cacheMaxAge
        timeUnit = requestBuilder.timeUnit
        headers = requestBuilder.headers
        queryParams = requestBuilder.queryParams
        kineClient = requestBuilder.kineClient
        executor = requestBuilder.executor
        logLevel = requestBuilder.logLevel
        converter = requestBuilder.converter
        file = requestBuilder.file
        progressListener = requestBuilder.progressListener
    }

    fun <F> execute(clazz: KineClass<F>): KineResponse<F>? {
        if (reqTAG == null) {
            reqTAG = requestUrl
            Logger.d("Request", "requestTag not specified using url as tag")
        }
        return RequestManager.executeRequest(buildRequest(), clazz)
    }

    private fun buildRequest(): Request {
        return if (file != null && progressListener != null) {
            DownloadRequest(
                file!!,
                progressListener!!, kineClient, converter,
                RequestData(
                    reqTAG!!,
                    requestUrl,
                    method,
                    requestBody ?: SimpleRequestBody(),
                    headers
                ),
                priority, retryPolicy,
                KineCacheControl(networkPolicy, cacheMaxAge, timeUnit),
                logLevel,
                executor
            )
        } else if (requestBody!=null && requestBody is MultiPartRequestBody) {
            UploadRequest(
                progressListener,
                kineClient, converter,
                RequestData(
                    reqTAG!!,
                    requestUrl,
                    method,
                    requestBody,
                    headers
                ),
                priority, retryPolicy, KineCacheControl(networkPolicy, cacheMaxAge, timeUnit),
                logLevel, executor
            )
        } else {
            Request(
                kineClient, converter,
                RequestData(
                    reqTAG!!,
                    requestUrl,
                    method,
                    requestBody ?: SimpleRequestBody(),
                    headers
                ),
                priority, retryPolicy, KineCacheControl(networkPolicy, cacheMaxAge, timeUnit),
                logLevel, executor
            )
        }
    }

    fun <F> execute(clazz: KineClass<F>, onSuccess: OnSuccess<F>?, onError: OnError?) {
        if (reqTAG == null) {
            reqTAG = requestUrl
            Logger.d("Request", "requestTag not specified using url as tag")
        }
        if (!RequestManager.isConnected()) {
            onError?.invoke(KineError(NoInternetException()))
            return
        }
        val request: Request = buildRequest()
        KineExecutorManager.executorSupplier.forNetworkTasks()
            .submit(object : PriorityRunnable(request.priority) {
                override fun run() {
                    try {
                        val response = RequestManager.executeRequest(request, clazz)
                        onCallbackThread {
                            if (response?.body != null) {
                                onSuccess?.invoke(response)
                            } else {
                                onError?.invoke(KineError(NullResponseException()))
                            }
                        }
                    } catch (exception: Throwable) {
                        onCallbackThread {
                            if (exception is NoClientFoundException || exception is NoConverterFoundException) {
                                throw exception
                            }
                            onError?.invoke(KineError(exception))
                        }
                    }
                }
            })
    }

    companion object {
        @Suppress("unused")
        fun clone(copy: KineRequest): KineRequest {
            val builder = RequestBuilder()
            builder.method = copy.method
            builder.url = copy.requestUrl
            builder.requestBody = copy.requestBody
            builder.retryPolicy = copy.retryPolicy
            builder.priority = copy.priority
            builder.reqTAG = copy.reqTAG
            builder.networkPolicy = copy.networkPolicy
            builder.cacheMaxAge = copy.cacheMaxAge
            builder.headers = copy.headers
            builder.kineClient = copy.kineClient
            builder.logLevel = copy.logLevel
            builder.converter = copy.converter
            builder.executor = copy.executor
            return KineRequest(builder)
        }

        fun post(url: String): RequestEncodedBodyBuilder {
            return RequestHttpMethodBuilder().post(url)
        }

        fun delete(url: String): RequestEncodedBodyBuilder {
            return RequestHttpMethodBuilder().delete(url)
        }

        fun put(url: String): RequestEncodedBodyBuilder {
            return RequestHttpMethodBuilder().put(url)
        }

        fun patch(url: String): RequestEncodedBodyBuilder {
            return RequestHttpMethodBuilder().patch(url)
        }

        fun get(url: String): RequestOptionsBuilder {
            return RequestHttpMethodBuilder().get(url)
        }

        fun head(url: String): RequestOptionsBuilder {
            return RequestHttpMethodBuilder().head(url)
        }

        fun upload(url: String): RequestMultiPartBodyBuilder {
            return RequestHttpMethodBuilder().upload(url)
        }

        fun method(url: String, method: Int): RequestOptionsBuilder {
            return RequestHttpMethodBuilder().method(url, method)
        }
    }

    /**
     * Supported request methods.
     */
    interface Method {
        companion object {
            const val GET = 0
            const val POST = 1
            const val PUT = 2
            const val DELETE = 3
            const val HEAD = 4
            const val PATCH = 5
        }
    }

    class RequestMultiPartBodyBuilder(url: String = "", method: Int = Method.GET) : RequestBuilder(url, method) {
        init {
            requestBody = MultiPartRequestBody()
        }
        fun addMultiPartParam(
            key: String,
            value: String,
            contentType: String?
        ): RequestMultiPartBodyBuilder {
            requestBody = requestBody ?: MultiPartRequestBody()
            if (requestBody is MultiPartRequestBody) {
                (this.requestBody!! as MultiPartRequestBody).addMultiPartParam(
                    key,
                    value,
                    contentType
                )
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

        fun addMultiPartFileParam(
            key: String,
            value: File,
            contentType: String?
        ): RequestMultiPartBodyBuilder {
            requestBody = requestBody ?: MultiPartRequestBody()
            if (requestBody is MultiPartRequestBody) {
                (this.requestBody!! as MultiPartRequestBody).addMultiPartFileParam(
                    key,
                    value,
                    contentType
                )
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

        fun addMultiPartParams(
            parts: HashMap<String, String>,
            contentType: String?
        ): RequestMultiPartBodyBuilder {
            requestBody = requestBody ?: MultiPartRequestBody()
            if (requestBody is MultiPartRequestBody) {
                (this.requestBody!! as MultiPartRequestBody)
                    .addMultiPartParams(parts, contentType)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

        fun addMultiPartFileParams(
            parts: HashMap<String, File>,
            contentType: String?
        ): RequestMultiPartBodyBuilder {
            requestBody = requestBody ?: MultiPartRequestBody()
            if (requestBody is MultiPartRequestBody) {
                (this.requestBody!! as MultiPartRequestBody)
                    .addMultiPartFileParams(parts, contentType)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

        fun addMultiPartFileListParams(
            parts: HashMap<String, List<File>>,
            contentType: String?
        ): RequestMultiPartBodyBuilder {
            requestBody = requestBody ?: MultiPartRequestBody()
            if (requestBody is MultiPartRequestBody) {
                (this.requestBody!! as MultiPartRequestBody)
                    .addMultiPartFileListParams(parts, contentType)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }
        fun setUploadListener(progressListener: ProgressListener): RequestMultiPartBodyBuilder {
            this.progressListener = progressListener
            return this
        }

        fun contentType(contentType: String): RequestMultiPartBodyBuilder {
            requestBody = requestBody ?: MultiPartRequestBody()
            this.requestBody?.setContentType(contentType)
            return this
        }
    }

    class RequestEncodedBodyBuilder(url: String = "", method: Int = Method.GET) :
        RequestBodyBuilder(url, method) {
        fun bodyParams(params: HashMap<String, String>?): RequestEncodedBodyBuilder {
            requestBody = requestBody ?: EncodedRequestBody()
            if (requestBody is EncodedRequestBody) {
                (this.requestBody!! as EncodedRequestBody).setBody(params)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

        fun addBodyParam(key: String, value: String): RequestEncodedBodyBuilder {
            requestBody = requestBody ?: EncodedRequestBody()
            if (requestBody is EncodedRequestBody) {
                (this.requestBody!! as EncodedRequestBody).addBodyParam(key, value)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

        fun addEncodedBodyParam(key: String, value: String): RequestEncodedBodyBuilder {
            requestBody = requestBody ?: EncodedRequestBody()
            if (requestBody is EncodedRequestBody) {
                (this.requestBody!! as EncodedRequestBody).addBodyParamEncoded(key, value)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

        fun encodedBodyParams(params: HashMap<String, String>?): RequestEncodedBodyBuilder {
            requestBody = requestBody ?: EncodedRequestBody()
            if (requestBody is EncodedRequestBody) {
                (this.requestBody!! as EncodedRequestBody).setBodyEncoded(params)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }
    }

    open class RequestBodyBuilder(url: String = "", method: Int = Method.GET) :
        RequestBuilder(url, method) {

        fun contentType(contentType: String): RequestBodyBuilder {
            requestBody = requestBody ?: SimpleRequestBody()
            this.requestBody?.setContentType(contentType)
            return this
        }

        /**
         * Sets the `bodyParams` and returns a reference to `RequestOptionsBuilder`
         *
         * @param params the `bodyParams` to set
         * @param contentType the encoding mediaType to use
         * @return a reference to this RequestBuilder
         */
        fun bodyParams(
            params: String?,
            contentType: String = ContentType.JSON.toString()
        ): RequestBodyBuilder {
            requestBody = requestBody ?: StringRequestBody()
            if (requestBody is StringRequestBody) {
                (this.requestBody!! as StringRequestBody).setBody(params, contentType)
            } else {
                throw IllegalArgumentException("Only one type of params is allowed per request either STRING,ENCODED,MULTIPART or any other")
            }
            return this
        }

    }

    interface RequestOptionsBuilder {
        fun addHeader(key: String, value: String): RequestOptionsBuilder
        fun headers(headers: HashMap<String, String?>?): RequestOptionsBuilder
        fun userAgent(userAgent: String): RequestOptionsBuilder
        fun addQueryParam(key: String, value: String): RequestOptionsBuilder
        fun queryParams(params: HashMap<String, String?>?): RequestOptionsBuilder
        fun tag(tag: String): RequestOptionsBuilder
        fun client(kineClient: KineClient?): RequestOptionsBuilder
        fun converter(converter: Converter?): RequestOptionsBuilder
        fun logLevel(level: Int): RequestOptionsBuilder
        fun notFromCache(): RequestOptionsBuilder
        fun doNotCache(): RequestOptionsBuilder
        fun onlyFromCache(): RequestOptionsBuilder
        fun onlyFromNetwork(): RequestOptionsBuilder
        fun cacheMaxAge(time: Int, timeUnit: TimeUnit): RequestOptionsBuilder
        fun priority(priority: Priority): RequestOptionsBuilder
        fun retryPolicy(retryPolicy: RetryPolicy?): RequestOptionsBuilder
        fun <F> responseAs(clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError)
        fun <F> responseAs(clazz: KineClass<F>, onSuccess: OnSuccess<F>, onError: OnError)

        @Throws(Throwable::class)
        fun <F> responseAs(clazz: Class<F>): KineResponse<F>?

        @Throws(Throwable::class)
        fun <F> responseAs(clazz: KineClass<F>): KineResponse<F>?
        fun downloadFile(
            file: File,
            progressListener: ProgressListener,
            onSuccess: OnSuccess<File>? = null,
            onError: OnError
        )

        fun downloadFile(file: File, progressListener: ProgressListener): KineResponse<File>?
        fun build(): KineRequest
    }

    interface RequestTypeBuilder {
        fun post(url: String): RequestEncodedBodyBuilder

        fun put(url: String): RequestEncodedBodyBuilder

        fun patch(url: String): RequestEncodedBodyBuilder

        fun delete(url: String): RequestEncodedBodyBuilder

        fun get(url: String): RequestOptionsBuilder

        fun head(url: String): RequestOptionsBuilder

        fun method(url: String, method: Int): RequestOptionsBuilder

        fun upload(url: String): RequestMultiPartBodyBuilder
    }

    /**
     * `KineRequest` requestBuilder static inner class.
     */
    open class RequestBuilder(var url: String = "", var method: Int = Method.GET) :
        RequestOptionsBuilder {

        var kineClient: KineClient? = null
        var converter: Converter? = null
        var retryPolicy: RetryPolicy? = null
        var reqTAG: String? = null
        var networkPolicy = 0
        var cacheMaxAge: Int = 0
        var timeUnit: TimeUnit = TimeUnit.SECONDS
        var priority: Priority = Priority.IMMEDIATE
        var headers: HashMap<String, String?>? = null
        var executor: Executor = KineExecutorManager.executorSupplier.forNetworkTasks()
        var requestBody: RequestBody? = SimpleRequestBody()
        var queryParams: HashMap<String, String?>? = null
        var logLevel = LogLevel.NO_LEVEL
        var file: File? = null
        var progressListener: ProgressListener? = null
        val USER_AGENT = "User-Agent"

        override fun notFromCache(): RequestOptionsBuilder {
            networkPolicy = KineCacheControl.NO_CACHE
            return this
        }

        override fun doNotCache(): RequestOptionsBuilder {
            networkPolicy = KineCacheControl.NO_STORE
            return this
        }

        override fun onlyFromCache(): RequestOptionsBuilder {
            networkPolicy = KineCacheControl.FORCE_CACHE
            return this
        }

        override fun onlyFromNetwork(): RequestOptionsBuilder {
            networkPolicy = KineCacheControl.FORCE_NETWORK
            return this
        }

        override fun priority(priority: Priority): RequestOptionsBuilder {
            this@RequestBuilder.priority = priority
            return this
        }

        /**
         * Sets the `requestHeader` and returns a reference to `RequestOptionsBuilder`
         *
         * @param headers the `requestHeader` to set
         * @return a reference to this RequestBuilder
         */
        override fun headers(headers: HashMap<String, String?>?): RequestOptionsBuilder {
            this@RequestBuilder.headers = headers
            return this
        }

        override fun addHeader(key: String, value: String): RequestOptionsBuilder {
            headers = headers ?: HashMap()
            headers!![key] = value
            return this
        }

        override fun userAgent(userAgent: String): RequestOptionsBuilder {
            headers = headers ?: HashMap()
            headers?.put(USER_AGENT, userAgent)
            return this
        }

        /**
         * Sets the `retryPolicy` and returns a reference to `RequestOptionsBuilder`
         *
         * @param retryPolicy the `retryPolicy` to set
         * @return a reference to this RequestBuilder
         */
        override fun retryPolicy(retryPolicy: RetryPolicy?): RequestOptionsBuilder {
            this@RequestBuilder.retryPolicy = retryPolicy
            return this
        }

        override fun <F> responseAs(clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError) {
            responseAs(DefaultKineClass(clazz), onSuccess, onError)
        }

        override fun <F> responseAs(
            clazz: KineClass<F>,
            onSuccess: OnSuccess<F>,
            onError: OnError
        ) {
            build().execute(clazz, onSuccess, onError)
        }

        override fun <F> responseAs(clazz: Class<F>): KineResponse<F>? {
            return build().execute(DefaultKineClass(clazz))
        }

        override fun <F> responseAs(clazz: KineClass<F>): KineResponse<F>? {
            return build().execute(clazz)
        }

        override fun downloadFile(
            file: File,
            progressListener: ProgressListener,
            onSuccess: OnSuccess<File>?,
            onError: OnError
        ) {
            this@RequestBuilder.file = file
            this@RequestBuilder.progressListener = progressListener
            converter(FileDownloadConverter())
            build().execute(DefaultKineClass(File::class.java), onSuccess, onError)
        }

        override fun downloadFile(
            file: File,
            progressListener: ProgressListener
        ): KineResponse<File>? {
            this@RequestBuilder.file = file
            this@RequestBuilder.progressListener = progressListener
            converter(FileDownloadConverter())
            return build().execute(DefaultKineClass(File::class.java))
        }

        override fun addQueryParam(key: String, value: String): RequestOptionsBuilder {
            queryParams = queryParams ?: HashMap()
            queryParams!![key] = value
            return this
        }

        override fun queryParams(params: HashMap<String, String?>?): RequestOptionsBuilder {
            queryParams = params
            return this
        }

        override fun cacheMaxAge(time: Int, timeUnit: TimeUnit): RequestOptionsBuilder {
            cacheMaxAge = time
            this@RequestBuilder.timeUnit = timeUnit
            return this
        }

        override fun client(kineClient: KineClient?): RequestOptionsBuilder {
            this@RequestBuilder.kineClient = kineClient
            return this
        }

        override fun converter(converter: Converter?): RequestOptionsBuilder {
            this@RequestBuilder.converter = converter
            return this
        }

        override fun logLevel(level: Int): RequestOptionsBuilder {
            logLevel = level
            return this
        }

        override fun tag(tag: String): RequestOptionsBuilder {
            reqTAG = tag
            return this
        }

        /**
         * Returns a `RequestBuilder` built from the parameters previously set.
         *
         * @return a `RequestBuilder` built with parameters of this `RequestBuilder.RequestBuilder`
         */
        override fun build(): KineRequest {
            return KineRequest(this@RequestBuilder)
        }

    }

    class RequestHttpMethodBuilder : RequestTypeBuilder {
        override fun post(url: String): RequestEncodedBodyBuilder {
            return RequestEncodedBodyBuilder(url, Method.POST)
        }

        override fun put(url: String): RequestEncodedBodyBuilder {
            return RequestEncodedBodyBuilder(url, Method.PUT)
        }

        override fun patch(url: String): RequestEncodedBodyBuilder {
            return RequestEncodedBodyBuilder(url, Method.PATCH)
        }

        override fun delete(url: String): RequestEncodedBodyBuilder {
            return RequestEncodedBodyBuilder(url, Method.DELETE)
        }

        override fun get(url: String): RequestOptionsBuilder {
            return method(url, Method.GET)

        }

        override fun head(url: String): RequestOptionsBuilder {
            return method(url, Method.HEAD)
        }

        /**
         * Sets the `method` and returns a reference to `RequestOptionsBuilder`
         *
         * @param method the `method` to set
         * @return a reference to this RequestBuilder
         */
        override fun method(url: String, method: Int): RequestOptionsBuilder {
            return if (method == Method.HEAD || method == Method.GET)
                RequestBuilder(url, method) else RequestEncodedBodyBuilder(url, method)
        }

        override fun upload(url: String): RequestMultiPartBodyBuilder {
            return RequestMultiPartBodyBuilder(url, Method.POST)
        }
    }
}