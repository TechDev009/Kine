package com.kine.android.request

import com.kine.request.ContentType
import com.kine.request.RequestBody
import org.json.JSONObject

class JsonRequestBody(jsonObject: JSONObject?):RequestBody(jsonObject?.toString(),ContentType.JSON.toString())