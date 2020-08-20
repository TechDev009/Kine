package com.kine.exceptions

import java.lang.Exception

class NoInternetException(msg: String?="Please check your internet connection"):Exception(msg)
{
}