# Kine

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.0-blue.svg)](https://kotlinlang.org) 

A simple and powerful HTTP networking library for Kotlin/Android.

## Features

- [x] Supports HTTP GET/POST/PUT/DELETE/HEAD/PATCH
- [x] Supports synchronous and asynchronous requests
- [x] Download file with progress
- [x] Get Image as bitmap from url
- [x] Response caching in disk 
- [x] OkHttp cache control full support
- [x] Supports OkHttp with ability to write your own Httpclient
- [x] Cancel in-flight request
- [x] Request timeout
- [x] Configuration using `Kime`
- [x] Log manager with option to turn logging off
- [x] Supports response deserialization into POJO/POKO with Gson or Moshi
- [x] Supports reactive programming via RxJava 2.x
- [x] Supports kotlin coroutines

## UpComing Features

- [x] Upload file (multipart/form-data)
- [x] Support for jackson
- [x] Bitmap caching and auto previous request cancellation for ImageLoading
- [x] New interceptor api for manipulating request pre execute

## Installation

Library is available on jcenter

### Gradle

``` Groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.kine:kine:<latest-version>' //for JVM
    compile 'com.kine:kine-android:<latest-version>' //for Android
    compile 'com.kine:kine-rxjava2:<latest-version>' //for RxJava2 support
    compile 'com.kine:kine-coroutine:<latest-version>' //for Coroutine support
    compile 'com.kine:kine-gson:<latest-version>' //for Gson support
    compile 'com.kine:kine-moshi:<latest-version>' //for Moshi support
    compile 'com.kine:kine-okhttp:<latest-version>' //for OkHttp support
    compile 'com.kine:kine-imageloader:<latest-version>' //for Imageloading support
}
```
