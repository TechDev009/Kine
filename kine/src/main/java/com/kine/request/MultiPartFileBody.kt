package com.kine.request

import java.io.File

class MultiPartFileBody(value: File, contentType:String?=null): MultiPartBody<File>(value, contentType)