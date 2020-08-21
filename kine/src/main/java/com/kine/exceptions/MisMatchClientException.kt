package com.kine.exceptions

class MisMatchClientException(msg: String?= "no kineClient found that can handle this type of request, " +
        "please set at least one client that can " +
        "handle this type of request" + " " + "through Kine class or individual request") : IllegalStateException(msg)