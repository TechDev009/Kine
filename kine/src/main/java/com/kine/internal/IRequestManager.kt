
package com.kine.internal

import com.kine.request.Request
import com.kine.response.KineResponse

/**
 * abstract internal class that takes care of caching , parsing and check other params for a request
 */
internal interface IRequestManager {

    fun <F> executeRequest(request: Request, clazz: KineClass<F>):KineResponse<F>?
}