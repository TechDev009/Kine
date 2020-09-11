package com.kine.request

import com.kine.cache.KineCacheControl
import com.kine.client.KineClient
import com.kine.converters.Converter
import com.kine.policies.Priority
import com.kine.policies.RetryPolicy
import java.util.concurrent.Executor


/**`
 * An RequestFields. This class is used to store all information associated with a single HTTP request.
 */
 open class RequestFields(val kineClient: KineClient?, val converter: Converter?,
                          val dataFields: RequestDataFields, var priority: Priority, var retryPolicy: RetryPolicy?,
                          val kineCacheControl: KineCacheControl, val logLevel: Int, var executor: Executor)