package com.kine

import com.kine.client.KineClient
import com.kine.connections.ConnectionChecker
import com.kine.converters.Converter
import com.kine.internal.RequestManager
import com.kine.log.LogLevel
import com.kine.log.Logger.setDisabledLogs
import com.kine.log.Logger.setLevel
import com.kine.policies.RetryPolicy
import java.util.*


/**
 * Global class for setting config for all request.
 */

@Suppress("unused")
class Kine private constructor(builder: Builder) {
    /**
     * `Kine` builder static inner class.
     */
    class Builder {
        internal var kineClients: ArrayList<KineClient>? = null
        internal var converters: ArrayList<Converter>? = null
        internal var headers: HashMap<String, String?>? = null
        internal var baseUrl: String? = null
        internal var retryPolicy: RetryPolicy? = null
        internal var networkPolicy = 0
        internal var connectionChecker: ConnectionChecker? = null
        internal var logLevel = LogLevel.NO_LEVEL
        internal var disableAllLogs = false

        fun baseUrl(url: String): Builder {
            baseUrl = url
            return this
        }

        fun addHeader(key: String, value: String): Builder {
            if (headers == null) {
                headers = HashMap()
            }
            headers!![key] = value
            return this
        }

        /**
         * Sets the `requestHeader` and returns a reference to `IRetryPolicy`
         *
         * @param params the `requestHeader` to set
         * @return a reference to this Builder
         */
        fun headers(params : HashMap<String, String?>?): Builder {
            headers = params
            return this
        }

        fun logLevel(level: Int): Builder {
            logLevel = level
            return this
        }

        fun connectionChecker(connectionChecker: ConnectionChecker?): Builder {
            this.connectionChecker = connectionChecker
            return this
        }

        fun disableAllLogs(disableAllLogs: Boolean): Builder {
            this.disableAllLogs = disableAllLogs
            return this
        }

        /**
         * Sets the `retryPolicy` and returns a reference to `IReqTAG`
         *
         * @param retryPolicy the `retryPolicy` to set
         * @return a reference to this Builder
         */
        fun retryPolicy(retryPolicy: RetryPolicy?): Builder {
            this.retryPolicy = retryPolicy
            return this
        }

        /**
         * Specifies the [CacheControl] to use for this request. You may specify additional policy
         * options using the varargs parameter.
         */
        fun cacheControl(networkPolicy: Int): Builder {
            this.networkPolicy = networkPolicy
            return this
        }

        /**
         * Sets the `KineClient` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param `kineClient` the `KineClient` to set
         * @return a reference to this Builder
         */
        fun client(kineClient: KineClient): Builder {
            this.kineClients= arrayListOf(kineClient)
            return this
        }
        fun clients(kineClient: ArrayList<KineClient>?): Builder {
            this.kineClients = kineClient
            return this
        }
        fun addClient(kineClient: KineClient): Builder {
            if (this.kineClients == null) {
                this.kineClients = ArrayList(2)
            }
            this.kineClients!!.add(kineClient)
            return this
        }
        fun converters(converters: ArrayList<Converter>?): Builder {
            this.converters = converters
            return this
        }

        fun converter(converter: Converter): Builder {
            this.converters = arrayListOf(converter)
            return this
        }

        fun addConverter(converters: Converter): Builder {
            this.converters = this.converters?: arrayListOf()
            this.converters!!.add(converters)
            return this
        }



        /**
         * Returns a `GlobalRequestBuilder` built from the parameters previously set.
         *
         * @return a `GlobalRequestBuilder` built with parameters of this `GlobalRequestBuilder.Builder`
         */
        fun build(): Kine {
            return Kine(this)
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
         fun cancelAllRequests(tag: String?=null) {
            RequestManager.cancelAllRequests(tag)
        }
    }

    init {
        RequestManager.setRetryPolicy(builder.retryPolicy)
        RequestManager.setNetworkPolicy(builder.networkPolicy)
        RequestManager.setHeaders(builder.headers)
        RequestManager.setClients(builder.kineClients)
        RequestManager.setConverters(builder.converters)
        RequestManager.setConnectionChecker(builder.connectionChecker)
        RequestManager.setBaseUrl(builder.baseUrl)
        setLevel(builder.logLevel)
        setDisabledLogs(builder.disableAllLogs)
    }
}