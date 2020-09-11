package com.kine.request

import com.kine.cache.KineCacheControl
import com.kine.client.KineClient
import com.kine.converters.Converter
import com.kine.extensions.ProgressListener
import com.kine.policies.Priority
import com.kine.policies.RetryPolicy
import java.util.concurrent.Executor

class UploadRequestFields(val progressListener:ProgressListener?, kineClient: KineClient?, converter: Converter?,
                          requestDataFields: RequestDataFields, priority: Priority, retryPolicy: RetryPolicy?,
                          kineCacheControl: KineCacheControl, logLevel: Int, executor: Executor
)
    :RequestFields(kineClient, converter, requestDataFields, priority, retryPolicy, kineCacheControl, logLevel,executor){

}