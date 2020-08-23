package com.kine.android

import androidx.lifecycle.MutableLiveData
import com.kine.KineRequest


fun <F> KineRequest.RequestOptionsBuilder.responseAs(clazz: Class<F>, liveData:MutableLiveData<F>) {
    return build().execute(clazz,onSuccess = { response->
        liveData.value = response.body
    },onError = {e->})
}

fun <F> KineRequest.RequestOptionsBuilder.responseBackgroundAs(clazz: Class<F>, liveData:MutableLiveData<F>) {
    val response = build().execute(clazz)
    if(response?.body!=null) {
        liveData.postValue(response.body)
    }
}