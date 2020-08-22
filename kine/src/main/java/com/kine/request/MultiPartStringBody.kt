package com.kine.request

class MultiPartStringBody(value:String, contentType:String?=null): MultiPartBody<String>(value, contentType)