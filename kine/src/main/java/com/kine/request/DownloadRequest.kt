package com.kine.request

import com.kine.cache.KineCacheControl
import com.kine.client.KineClient
import com.kine.converters.Converter
import com.kine.extensions.ProgressListener
import com.kine.policies.Priority
import com.kine.policies.RetryPolicy
import java.io.File
import java.util.concurrent.Executor

 class DownloadRequest(val file: File, val progressListener:ProgressListener, kineClient: KineClient?, converter: Converter?, requestData: RequestData, priority: Priority, retryPolicy: RetryPolicy?,
                       kineCacheControl: KineCacheControl, logLevel: Int, executor: Executor)
     :Request(kineClient, converter, requestData, priority, retryPolicy, kineCacheControl, logLevel,executor){

}