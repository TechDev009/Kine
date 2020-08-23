package com.kine

import com.kine.exceptions.HttpStatusCodeException
import com.kine.extensions.responseAsJson
import com.kine.extensions.responseAsJsonArray
import com.kine.log.LogLevel
import com.kine.policies.DefaultRetryPolicy
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference


class JsonApiTest {

    private val mockServer = MockWebServer()
    private val testStringResponse = JSONObject(HashMap<Any?,Any?>().apply{put("test","response")}).toString()

    private val testJsonArrayResponse = JSONArray(arrayListOf<String>().apply{add("test")
        add("response")}).toString()
    @Before
    fun init() {
        mockServer.start()
        Kine.Builder()

            .retryPolicy(DefaultRetryPolicy(12500,0,0f))
            .logLevel(LogLevel.ERROR)
            .build()
    }

    @Test
    fun httpResponseSuccessTest() {
        mockServer.enqueue(MockResponse().setResponseCode(200).setBody(testStringResponse))
        val responseRef = AtomicReference<JSONObject>()
        val countDownLatch = CountDownLatch(1)

        KineRequest.get(mockServer.url("/").toString())
            .responseAsJson({ response ->
                responseRef.set(response.body)
                countDownLatch.countDown()
            }, {
                Assert.assertTrue(false)
            })
        Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS))
        Assert.assertEquals(testStringResponse, responseRef.get().toString())

    }
    @Test
    fun httpResponseFailedTest() {
        mockServer.enqueue(MockResponse().setResponseCode(402).setBody(testStringResponse))
        val errorDetailRef = AtomicReference<Throwable>()
        val countDownLatch = CountDownLatch(1)
        KineRequest.get(mockServer.url("/").toString())
            .responseAsJson({
                Assert.assertTrue(false)
            }, {e->
                errorDetailRef.set(e.exception)
                countDownLatch.countDown()
            })

        Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS))
        Assert.assertTrue(errorDetailRef.get() is HttpStatusCodeException)
    }
    @Test
    fun httpResponseSuccessSyncTest() {
        mockServer.enqueue(MockResponse().setResponseCode(200).setBody(testStringResponse))
        val response = KineRequest.get(mockServer.url("/").toString()).responseAsJson()
        Assert.assertEquals(testStringResponse, response?.body.toString())
        Assert.assertEquals(200, response?.statusCode)
    }
    @Test
    fun httpResponseFailedSyncTest() {
        mockServer.enqueue(MockResponse().setResponseCode(402).setBody(testStringResponse))
        try {
            KineRequest.get(mockServer.url("/").toString()).responseAsJson()
            Assert.assertTrue(false)
        }catch (e:Throwable){
            Assert.assertTrue(e is HttpStatusCodeException)
        }
    }
    @Test
    fun httpResponseSuccessSyncArrayTest() {
        mockServer.enqueue(MockResponse().setResponseCode(200).setBody(testJsonArrayResponse))
        val response = KineRequest.get(mockServer.url("/").toString()).responseAsJsonArray()
        Assert.assertEquals(testJsonArrayResponse, response?.body.toString())
        Assert.assertEquals(200, response?.statusCode)
    }
    @Test
    fun httpResponseFailedSyncArrayTest() {
        mockServer.enqueue(MockResponse().setResponseCode(402).setBody(testJsonArrayResponse))
        try {
            KineRequest.get(mockServer.url("/").toString()).responseAsJsonArray()
            Assert.assertTrue(false)
        }catch (e:Throwable){
            Assert.assertTrue(e is HttpStatusCodeException)
        }
    }
}