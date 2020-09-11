
package com.kine.internal

import com.kine.request.RequestFields
import com.kine.response.KineResponse

/**
 * abstract internal class that takes care of caching , parsing and check other params for a request
 */
internal interface IRequestManager {

    fun <F> executeRequest(requestFields: RequestFields, clazz: KineClass<F>):KineResponse<F>?
}