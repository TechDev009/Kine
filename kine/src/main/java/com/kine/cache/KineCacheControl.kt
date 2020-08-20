package com.kine.cache

import java.util.concurrent.TimeUnit

class KineCacheControl(var networkPolicy:Int = NO_CACHE_CONTROL, val cacheMaxAge:Int=0, val timeUnit: TimeUnit){
    companion object{
        const val NO_CACHE_CONTROL = 0
        const val FORCE_CACHE = 1
        const val FORCE_NETWORK = 2
        const val NO_CACHE = 3
        const val NO_STORE = 4
        const val CACHE_FOR_TIME =5
    }
}
