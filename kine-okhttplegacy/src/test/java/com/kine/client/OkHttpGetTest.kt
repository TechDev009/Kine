package com.kine.client

import com.kine.Kine
import com.kine.KineRequest
import com.kine.exceptions.HttpStatusCodeException
import com.kine.log.LogLevel
import com.kine.policies.DefaultRetryPolicy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class OkHttpGetTest {

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

        KineRequest.get(mockServer.url("/").toString())
            .responseAs(String::class.java, { response ->
                responseRef.set(response.body)
                countDownLatch.countDown()
            }, {
                assertTrue(false)
            })
        assertTrue(countDownLatch.await(3, TimeUnit.SECONDS))
        assertEquals(testStringResponse, responseRef.get())

    }
    @Test
    fun httpResponseFailedTest() {
        mockServer.enqueue(MockResponse().setResponseCode(402).setBody(testStringResponse))
        val errorDetailRef = AtomicReference<Throwable>()
        val countDownLatch = CountDownLatch(1)
        KineRequest.get(mockServer.url("/").toString())
            .responseAs(String::class.java, {
                assertTrue(false)
            }, {e->
                errorDetailRef.set(e.exception)
                countDownLatch.countDown()
            })

        assertTrue(countDownLatch.await(3, TimeUnit.SECONDS))
        assertTrue(errorDetailRef.get() is HttpStatusCodeException)
    }
    @Test
    fun httpResponseSuccessSyncTest() {
        mockServer.enqueue(MockResponse().setResponseCode(200).setBody(testStringResponse))
        val response = KineRequest.get(mockServer.url("/").toString())
            .responseAs(String::class.java)
        assertEquals(testStringResponse, response?.body)
        assertEquals(200, response?.statusCode)
    }
    @Test
    fun httpResponseFailedSyncTest() {
        mockServer.enqueue(MockResponse().setResponseCode(402).setBody(testStringResponse))
        try {
            KineRequest.get(mockServer.url("/").toString()).responseAs(String::class.java)
            assertTrue(false)
        }catch (e:Throwable){
            assertTrue(e is HttpStatusCodeException)
        }
    }

}