package com.kine.client

import com.kine.Kine
import com.kine.KineRequest
import com.kine.exceptions.HttpStatusCodeException
import com.kine.log.LogLevel
import com.kine.policies.DefaultRetryPolicy
import com.kine.request.ContentType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class OkHttpDeleteTest {
    private val mockServer = MockWebServer()
    private val testStringResponse = "testing okhttpresponse"
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
        val responseRef = AtomicReference<String>()
        val countDownLatch = CountDownLatch(1)

        KineRequest.delete(mockServer.url("/").toString())
            .bodyParams(testStringResponse,ContentType.STRING.toString())
            .responseAs(String::class.java, { response ->
                responseRef.set(response.body)
                countDownLatch.countDown()
            }, {
                Assert.assertTrue(false)
            })
        Assert.assertTrue(countDownLatch.await(3, TimeUnit.SECONDS))
        Assert.assertEquals(testStringResponse, responseRef.get())

    }
    @Test
    fun httpResponseFailedTest() {
        mockServer.enqueue(MockResponse().setResponseCode(402).setBody(testStringResponse))
        val errorDetailRef = AtomicReference<Throwable>()
        val countDownLatch = CountDownLatch(1)
        KineRequest.delete(mockServer.url("/").toString())
            .responseAs(String::class.java, {
                Assert.assertTrue(false)
            }, {e->
                errorDetailRef.set(e.exception)
                countDownLatch.countDown()
            })

        Assert.assertTrue(countDownLatch.await(3, TimeUnit.SECONDS))
        Assert.assertTrue(errorDetailRef.get() is HttpStatusCodeException)
    }
    @Test
    fun httpResponseSuccessSyncTest() {
        mockServer.enqueue(MockResponse().setResponseCode(200).setBody(testStringResponse))
        val response = KineRequest.delete(mockServer.url("/").toString())
            .responseAs(String::class.java)
        Assert.assertEquals(testStringResponse, response?.body)
        Assert.assertEquals(200, response?.statusCode)
    }
    @Test
    fun httpResponseFailedSyncTest() {
        mockServer.enqueue(MockResponse().setResponseCode(402).setBody(testStringResponse))
        try {
            KineRequest.delete(mockServer.url("/").toString()).responseAs(String::class.java)
            Assert.assertTrue(false)
        }catch (e:Throwable){
            Assert.assertTrue(e is HttpStatusCodeException)
        }
    }
}