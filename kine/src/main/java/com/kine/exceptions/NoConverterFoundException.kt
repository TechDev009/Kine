package com.kine.exceptions

class NoConverterFoundException(msg: String?="no converter specified, please set at least one converter"
        + "through Kine class or through individual request") : IllegalStateException(msg)