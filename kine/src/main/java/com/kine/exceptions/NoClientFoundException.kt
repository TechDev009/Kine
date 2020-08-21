package com.kine.exceptions

class NoClientFoundException(msg: String?= "no kineClient found" +
        "please set at least one client through Kine class or individual request") : IllegalStateException(msg)