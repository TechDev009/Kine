package com.kine.test

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import com.kine.KineRequest
import com.kine.android.extensions.httpPost
import com.kine.converters.extensions.fromJsonArray
import com.kine.coroutine.responseAsCoroutine
import com.kine.extensions.httpGet
import com.kine.response.KineResponse
import com.kine.rxnetworking.toFlowable
import com.kine.rxnetworking.toObservable
import com.kine.rxnetworking.toSingle
import com.kine.test.model.Post
import io.reactivex.FlowableSubscriber
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.reactivestreams.Subscription

/** for fetching data from web **/
object NetworkUtils {
    val GET_URL = "users?page=2"
    val POST_URL = "users"
    fun <T> getRequest(clazz: Class<T>, timeTextView: TextView, responseTextView: TextView,
                       func: ((KineRequest.RequestOptionsBuilder) -> KineRequest.RequestOptionsBuilder)?=null) {
        val time = System.currentTimeMillis()
        GET_URL.httpGet().apply { func?.invoke(this) }.responseAs(clazz, { response ->
            Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
            timeTextView.text = ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                    " total time:${System.currentTimeMillis() - time}")
            responseTextView.text = response.body.toString()
        }, { e ->
            e.printStackTrace()
            responseTextView.text = e.message()
        })
    }
    fun getJsonArrayRequest(timeTextView: TextView, responseTextView: TextView) {
        val time = System.currentTimeMillis()
        "https://jsonplaceholder.typicode.com/posts".httpGet().responseAs(JSONArray::class.java,{ response->
            Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
            timeTextView.text = ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                    " total time:${System.currentTimeMillis() - time}")

            responseTextView.text =  Gson().fromJsonArray<Post>(response.body.toString())[0].title
        }, { e ->
            e.printStackTrace()
            responseTextView.text = e.message()
        })
    }
    fun <T> getRxRequest(clazz: Class<T>, timeTextView: TextView, responseTextView: TextView) {
        val time = System.currentTimeMillis()
        GET_URL.httpGet().toSingle(clazz).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<KineResponse<T>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(response: KineResponse<T>) {
                    Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
                    timeTextView.text =
                        ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                                " total time:${System.currentTimeMillis() - time}")
                    responseTextView.text = response.body.toString()
                }

                @SuppressLint("SetTextI18n")
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    responseTextView.text = "error is " + (e.localizedMessage ?: e.message)
                }

            })

    }

    @Suppress("NAME_SHADOWING")
    @SuppressLint("SetTextI18n")
    fun <T> getCoroutineRequest(
        clazz: Class<T>,
        timeTextView: TextView,
        responseTextView: TextView
    ) {
        val time = System.currentTimeMillis()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = GET_URL.httpGet().responseAsCoroutine(clazz = clazz)
                response?.let { response ->
                    Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
                    timeTextView.text =
                        ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                                " total time:${System.currentTimeMillis() - time}")
                    responseTextView.text = response.body.toString()
                }
            }catch (e:Exception){
                e.printStackTrace()
                responseTextView.text = "error is "+e.localizedMessage
            }
        }
    }

    fun <T> getRxFRequest(clazz: Class<T>, timeTextView: TextView, responseTextView: TextView) {
        val time = System.currentTimeMillis()
        GET_URL.httpGet().toFlowable(clazz).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : FlowableSubscriber<KineResponse<T>> {

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    responseTextView.text = e.localizedMessage
                }

                override fun onSubscribe(s: Subscription) {
                    s.request(1)
                }

                override fun onNext(response: KineResponse<T>) {
                    Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
                    timeTextView.text =
                        ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                                " total time:${System.currentTimeMillis() - time}")
                    responseTextView.text = response.body.toString()
                }

                @SuppressLint("SetTextI18n")
                override fun onComplete() {
                    responseTextView.text = "completed"
                }

            })
    }

    fun <T> getRxORequest(clazz: Class<T>, timeTextView: TextView, responseTextView: TextView) {
        val time = System.currentTimeMillis()
        GET_URL.httpGet().toObservable(clazz).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object :
                Observer<KineResponse<T>?> {

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    responseTextView.text = e.localizedMessage
                }

                override fun onNext(response: KineResponse<T>) {
                    Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
                    timeTextView.text =
                        ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                                " total time:${System.currentTimeMillis() - time}")
                    responseTextView.text = response.body.toString()
                }

                @SuppressLint("SetTextI18n")
                override fun onComplete() {
                    responseTextView.text = "completed"
                }

                override fun onSubscribe(d: Disposable) {
                }

            })
    }

    fun <T> postRequest(
        clazz: Class<T>, timeTextView: TextView, responseTextView: TextView,
        func: (KineRequest.RequestBodyBuilder) -> KineRequest.RequestOptionsBuilder
    ) {
        val time = System.currentTimeMillis()
        POST_URL.httpPost(JSONObject().put("name", "yodo").put("job", "test")).apply {
            func(this)
        }.responseAs(clazz, { response ->
            Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
            timeTextView.text = ("time:${response.networkTimeMs} parse time:${response.parseTime} " +
                    "total time:${System.currentTimeMillis() - time}")
            responseTextView.text = response.body.toString()
        }, { e ->
            e.printStackTrace()
            responseTextView.text = e.message()
        })
    }
}