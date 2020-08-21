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

open class KineRequest private constructor(builder: Builder) {
    private var method = Method.POST
    private val requestUrl: String
    private val requestBody: RequestBody
    private val kineClient: KineClient?
    private val converter: Converter?
    private val retryPolicy: RetryPolicy?
    private val priority: Priority
    private var reqTAG: String?
    private var networkPolicy = 0
    private val cacheMaxAge: Int
    private var timeUnit:TimeUnit=TimeUnit.SECONDS
    private val headers: HashMap<String, String?>?
    private val queryParams: HashMap<String, String?>?
    private var executor: Executor = KineExecutorManager.executorSupplier.forNetworkTasks()
    private var logLevel = LogLevel.NO_LEVEL
    private var file: File? = null
    private var progressListener: ProgressListener? = null

    init {
        method = builder.method
        requestUrl = builder.requestUrl
        requestBody = builder.requestBody
        retryPolicy = builder.retryPolicy
        reqTAG = builder.reqTAG
        networkPolicy = builder.networkPolicy
        priority = builder.priority
        cacheMaxAge = builder.cacheMaxAge
        timeUnit= builder.timeUnit
        headers = builder.headers
        queryParams = builder.queryParams
        kineClient = builder.kineClient
        executor = builder.executor
        logLevel = builder.logLevel
        converter = builder.converter
        file = builder.file
        progressListener = builder.progressListener
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
                RequestData(reqTAG!!, requestUrl, method, requestBody, headers),
                priority, retryPolicy,
                KineCacheControl(networkPolicy, cacheMaxAge,timeUnit),
                logLevel,
                executor
            )
        } else {
            Request(
                kineClient, converter,
                RequestData(reqTAG!!, requestUrl, method, requestBody, headers),
                priority, retryPolicy, KineCacheControl(networkPolicy, cacheMaxAge,timeUnit),
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
            val builder = Builder()
            builder.method = copy.method
            builder.requestUrl = copy.requestUrl
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

        fun post(params: String?): IBuildUrl {
            return Builder().post(params)
        }

        fun delete(params: String?): IBuildUrl {
            return Builder().delete(params)
        }

        fun put(params: String?): IBuildUrl {
            return Builder().put(params)
        }

        fun patch(params: String?): IBuildUrl {
            return Builder().patch(params)
        }

        fun get(): IBuildUrl {
            return Builder().get()
        }

        fun head(): IBuildUrl {
            return Builder().head()
        }

        fun method(method: Int): IBuildUrl {
            return Builder().method(method)
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

    interface IBuildUrl {
        fun url(url: String): IBuildOptions
    }

    interface IBuildOptions {
        fun addHeader(key: String, value: String): IBuildOptions
        fun headers(headers: HashMap<String, String?>?): IBuildOptions
        fun userAgent(userAgent: String): IBuildOptions
        fun contentType(contentType: String): IBuildOptions
        fun bodyParams(params: String?, contentType: String = ContentType.JSON.toString()): IBuildOptions
        fun addBodyParam(key: String, value: String): IBuildOptions
        fun addEncodedBodyParam(key: String, value: String): IBuildOptions
        fun bodyParams(params: HashMap<String, String>?): IBuildOptions
        fun encodedBodyParams(params: HashMap<String, String>?): IBuildOptions
        fun addQueryParam(key: String, value: String): IBuildOptions
        fun queryParams(params: HashMap<String, String?>?): IBuildOptions
        fun tag(tag: String): IBuildOptions
        fun client(kineClient: KineClient?): IBuildOptions
        fun converter(converter: Converter?): IBuildOptions
        fun logLevel(level: Int): IBuildOptions
        fun notFromCache(): IBuildOptions
        fun doNotCache(): IBuildOptions
        fun onlyFromCache(): IBuildOptions
        fun onlyFromNetwork(): IBuildOptions
        fun cacheMaxAge(time: Int, timeUnit: TimeUnit): IBuildOptions
        fun priority(priority: Priority): IBuildOptions
        fun retryPolicy(retryPolicy: RetryPolicy?): IBuildOptions
        fun <F> responseAs(clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError)
        fun <F> responseAs(clazz: KineClass<F>, onSuccess: OnSuccess<F>, onError: OnError)
        @Throws(Throwable::class)
        fun <F> responseAs(clazz: Class<F>): KineResponse<F>?
        @Throws(Throwable::class)
        fun <F> responseAs(clazz: KineClass<F>): KineResponse<F>?
        fun downloadFile(file: File, progressListener: ProgressListener, onSuccess: OnSuccess<File>? = null, onError: OnError)
        fun downloadFile(file: File, progressListener: ProgressListener):KineResponse<File>?
        fun build(): KineRequest
    }

    interface IBuildRequestType {
        fun post(
            params: String? = null,
            contentType: String = ContentType.JSON.toString()
        ): IBuildUrl

        fun put(
            params: String? = null,
            contentType: String = ContentType.JSON.toString()
        ): IBuildUrl

        fun patch(
            params: String? = null,
            contentType: String = ContentType.JSON.toString()
        ): IBuildUrl

        fun delete(
            params: String? = null,
            contentType: String = ContentType.JSON.toString()
        ): IBuildUrl

        fun get(): IBuildUrl

        fun head(): IBuildUrl

        fun method(method: Int): IBuildUrl
    }

    /**
     * `KineRequest` builder static inner class.
     */
    class Builder : IBuildRequestType {
        var method = Method.GET
        var requestUrl: String = ""
        var kineClient: KineClient? = null
        var converter: Converter? = null
        var retryPolicy: RetryPolicy? = null
        var reqTAG: String? = null
        var networkPolicy = 0
        var cacheMaxAge: Int = 0
        var timeUnit:TimeUnit=TimeUnit.SECONDS
        var priority: Priority = Priority.IMMEDIATE
        var headers: HashMap<String, String?>? = null
        var executor: Executor = KineExecutorManager.executorSupplier.forNetworkTasks()
        var requestBody = RequestBody()
        var queryParams: HashMap<String, String?>? = null
        var logLevel = LogLevel.NO_LEVEL
        var file: File? = null
        var progressListener: ProgressListener? = null
        val USER_AGENT = "User-Agent"
        private val iBuildOptions: IBuildOptions = object : IBuildOptions {

            override fun notFromCache(): IBuildOptions {
                networkPolicy = KineCacheControl.NO_CACHE
                return this
            }

            override fun doNotCache(): IBuildOptions {
                networkPolicy = KineCacheControl.NO_STORE
                return this
            }
            override fun onlyFromCache(): IBuildOptions {
                networkPolicy = KineCacheControl.FORCE_CACHE
                return this
            }

            override fun onlyFromNetwork(): IBuildOptions {
                networkPolicy = KineCacheControl.FORCE_NETWORK
                return this
            }
            override fun priority(priority: Priority): IBuildOptions {
                this@Builder.priority = priority
                return this
            }

            override fun contentType(contentType: String): IBuildOptions {
                this@Builder.requestBody.setContentType(contentType)
                return this
            }

            /**
             * Sets the `bodyParams` and returns a reference to `IBuildOptions`
             *
             * @param params the `bodyParams` to set
             * @param contentType the encoding mediaType to use
             * @return a reference to this Builder
             */
            override fun bodyParams(params: String?, contentType: String): IBuildOptions {
                this@Builder.requestBody.setBody(params,contentType)
                return this
            }

            override fun bodyParams(params: HashMap<String, String>?): IBuildOptions {
                requestBody.setBody(params)
                return this
            }

            override fun addBodyParam(key: String, value: String): IBuildOptions {
                requestBody.addBodyParam(key,value)
                return this
            }

            override fun addEncodedBodyParam(key: String, value: String): IBuildOptions {
                requestBody.addBodyParamEncoded(key, value)
                return this
            }

            override fun encodedBodyParams(params: HashMap<String, String>?): IBuildOptions {
                requestBody.setBodyEncoded(params)
                return this
            }

            /**
             * Sets the `requestHeader` and returns a reference to `IBuildOptions`
             *
             * @param headers the `requestHeader` to set
             * @return a reference to this Builder
             */
            override fun headers(headers: HashMap<String, String?>?): IBuildOptions {
                this@Builder.headers = headers
                return this
            }

            override fun addHeader(key: String, value: String): IBuildOptions {
                headers = headers ?: HashMap()
                headers!![key] = value
                return this
            }

            override fun userAgent(userAgent: String): IBuildOptions {
                headers = headers ?: HashMap()
                headers?.put(USER_AGENT, userAgent)
                return this
            }

            /**
             * Sets the `retryPolicy` and returns a reference to `IBuildOptions`
             *
             * @param retryPolicy the `retryPolicy` to set
             * @return a reference to this Builder
             */
            override fun retryPolicy(retryPolicy: RetryPolicy?): IBuildOptions {
                this@Builder.retryPolicy = retryPolicy
                return this
            }
            override fun <F> responseAs(clazz: Class<F>, onSuccess: OnSuccess<F>, onError: OnError) {
                 responseAs(DefaultKineClass(clazz), onSuccess, onError)
            }

            override fun <F> responseAs(clazz: KineClass<F>, onSuccess: OnSuccess<F>, onError: OnError) {
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
                this@Builder.file = file
                this@Builder.progressListener = progressListener
                converter(FileDownloadConverter())
                build().execute(DefaultKineClass(File::class.java), onSuccess, onError)
            }

            override fun downloadFile(file: File, progressListener: ProgressListener): KineResponse<File>? {
                this@Builder.file = file
                this@Builder.progressListener = progressListener
                converter(FileDownloadConverter())
                return build().execute(DefaultKineClass(File::class.java))
            }

            override fun addQueryParam(key: String, value: String): IBuildOptions {
                queryParams = queryParams ?: HashMap()
                queryParams!![key] = value
                return this
            }

            override fun queryParams(params: HashMap<String, String?>?): IBuildOptions {
                queryParams = params
                return this
            }

            override fun cacheMaxAge(time: Int, timeUnit: TimeUnit): IBuildOptions {
                cacheMaxAge = time
                this@Builder.timeUnit = timeUnit
                return this
            }

            override fun client(kineClient: KineClient?): IBuildOptions {
                this@Builder.kineClient = kineClient
                return this
            }

            override fun converter(converter: Converter?): IBuildOptions {
                this@Builder.converter = converter
                return this
            }

            override fun logLevel(level: Int): IBuildOptions {
                logLevel = level
                return this
            }

            override fun tag(tag: String): IBuildOptions {
                reqTAG = tag
                return this
            }

            /**
             * Returns a `RequestBuilder` built from the parameters previously set.
             *
             * @return a `RequestBuilder` built with parameters of this `RequestBuilder.Builder`
             */
            override fun build(): KineRequest {
                return KineRequest(this@Builder)
            }


        }
        private val iBuildUrl: IBuildUrl = object : IBuildUrl {
            /**
             * Sets the `requestUrl` and returns a reference to `IJsonObject`
             *
             * @param url the `requestUrl` to set
             * @return a reference to this Builder
             */
            override fun url(url: String): IBuildOptions {
                requestUrl = url
                return iBuildOptions
            }
        }

        override fun post(params: String?, contentType: String): IBuildUrl {
            this@Builder.requestBody.setBody(params,contentType)
            method(Method.POST)
            return iBuildUrl
        }

        override fun put(params: String?, contentType: String): IBuildUrl {
            this@Builder.requestBody.setBody(params,contentType)
            method(Method.PUT)
            return iBuildUrl
        }

        override fun patch(params: String?, contentType: String): IBuildUrl {
            this@Builder.requestBody.setBody(params,contentType)
            method(Method.PATCH)
            return iBuildUrl
        }

        override fun delete(params: String?, contentType: String): IBuildUrl {
            this@Builder.requestBody.setBody(params,contentType)
            method(Method.DELETE)
            return iBuildUrl
        }

        override fun get(): IBuildUrl {
            method(Method.GET)
            return iBuildUrl
        }

        override fun head(): IBuildUrl {
            method(Method.HEAD)
            return iBuildUrl
        }

        /**
         * Sets the `method` and returns a reference to `IRequestUrl`
         *
         * @param method the `method` to set
         * @return a reference to this Builder
         */
        override fun method(method: Int): IBuildUrl {
            this.method = method
            return iBuildUrl
        }
    }


}