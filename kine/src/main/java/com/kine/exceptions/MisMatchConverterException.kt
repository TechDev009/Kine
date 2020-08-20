package com.kine.exceptions

import java.lang.IllegalArgumentException

class MisMatchConverterException(msg:String= "the converter passed cannot handle " + "this type of response returned by this request" +
", please set a converter that can " + "handle this type of response" +
" " + "through converter() method"):IllegalArgumentException(msg)