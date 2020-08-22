package com.kine.android.request

import com.kine.request.ContentType
import com.kine.request.StringRequestBody
import org.json.JSONObject

class JsonRequestBody(jsonObject: JSONObject?):StringRequestBody(jsonObject?.toString(),ContentType.JSON.toString())