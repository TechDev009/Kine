package com.kine.test

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.kine.Kine
import com.kine.converters.GsonConverter
import com.kine.converters.MoshiConverter
import com.kine.extensions.downloadTo
import com.kine.imageloader.loadBitmapAndResponseFromUrl
import com.kine.log.LogLevel
import com.kine.test.model.CreateUserResponse
import com.kine.test.model.UserListResponse
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_main.*
import org.json.JSONObject
import java.io.File


/**
 * A placeholder fragment containing a simple view.
 */
class TestFragment : Fragment() {
    var pos = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Kine.Builder()
            .baseUrl(ConfigUtils.dummyBaseUrl)
            .logLevel(LogLevel.ERROR)
            .build()
       send.setOnClickListener {
            when (pos) {
                0 -> {
                    NetworkUtils.getRequest(UserListResponse::class.java, parseTime, resp){
                        it.converter(GsonConverter())
                    }
                }
                1 -> {
                    NetworkUtils.postRequest(CreateUserResponse::class.java, parseTime, resp)
                    { it.converter(GsonConverter()).bodyParams(hashMapOf())}
                }
                2 -> {
                    NetworkUtils.getRequest(UserListResponse::class.java, parseTime, resp)
                    {it.converter(MoshiConverter()) }
                }
                3 -> {
                    NetworkUtils.postRequest(CreateUserResponse::class.java, parseTime, resp)
                    {it.converter(MoshiConverter()) }
                }
                4 -> {
                    NetworkUtils.getRequest(JSONObject::class.java, parseTime, resp)
                }
                5 -> {
                    NetworkUtils.postRequest(JSONObject::class.java, parseTime, resp)
                    { it -> it }
                }
                6 -> {
                    NetworkUtils.getRequest(String::class.java, parseTime, resp)
                }
                7 -> {
                    NetworkUtils.postRequest(String::class.java, parseTime, resp)
                    { it -> it }
                }
                8 -> {
                    testRequest()
                }
                9 -> {
                    downloadImage()
                }
                10 -> {
                    NetworkUtils.getRxRequest(UserListResponse::class.java, parseTime, resp)
                }
                11 -> {
                    NetworkUtils.getRxFRequest(UserListResponse::class.java, parseTime, resp)
                }
                12 -> {
                    NetworkUtils.getRxORequest(UserListResponse::class.java, parseTime, resp)
                }
                13 -> {
                    NetworkUtils.getCoroutineRequest(UserListResponse::class.java, parseTime, resp)
                }
                14 -> {
                    NetworkUtils.getJsonArrayRequest( parseTime, resp)
                }
                else->{

                }
            }
        }
        val array: MutableList<String?> = ArrayList()
        array.add("Gson Get Request")
        array.add("Gson Post Request")
        array.add("Moshi Get Request")
        array.add("Moshi Post Request")
        array.add("Json Get Request")
        array.add("Json Post Request")
        array.add("String Get Request")
        array.add("String Post Request")
        array.add("Image Loading")
        array.add("Download File")
        array.add("RX Single Request")
        array.add("RX Flowable Request")
        array.add("RX Observable Request")
        array.add("Coroutine Request")
        array.add("JsonArray Get Request")
        spinner.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, array)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                Log.e("pos", "pos is$position")
                this@TestFragment.pos  = position

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

    private fun downloadImage() {
        val time = System.currentTimeMillis()
        "http://speedtest.ftp.otenet.gr/files/test10Mb.db".downloadTo(
            File(Environment.getExternalStorageDirectory(),"test10Mb.db"),{downloaded,total->
            Log.e("progress", (downloaded*100/total).toString())
            activity?.runOnUiThread {
                resp!!.text = "progress ${((downloaded*100)/total)}"
            }
        }, { response ->
                Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
            parseTime!!.text = ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                        " total time:${System.currentTimeMillis() - time}").trimIndent()
            resp!!.text ="saved as "+ response.response?.path?:"null"
            }, { e ->
            e.printStackTrace()
            activity?.runOnUiThread {
               resp.text = e.message()
            }
            })
    }

    private fun testRequest() {
        val time = System.currentTimeMillis()
        "http://i.imgur.com/2M7Hasn.png".loadBitmapAndResponseFromUrl( { response ->
            Log.e("response1", response.networkTimeMs.toString() + " " + response.parseTime)
            parseTime!!.text = ("time:${response.networkTimeMs} parse time:${response.parseTime}" +
                    " total time:${System.currentTimeMillis() - time}").trimIndent()
            resp!!.text = response.response.toString()
            image!!.setImageBitmap(response.response)
        }, { e ->
            resp.text = e.message()
        })
    }

}
