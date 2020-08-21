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

## Upcoming Planned Features

- Upload file (multipart/form-data)
- Support for jackson parser
- Bitmap caching and auto previous request cancellation for ImageLoading
- New interceptor api for manipulating request pre execute

## Installation

Library is available on jcenter

### Gradle

``` Groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.kine:kine:1.0.0' //for JVM
    compile 'com.kine:kine-android:1.0.0' //for Android
    compile 'com.kine:kine-rxjava2:2.2.19.0' //for RxJava2 support
    compile 'com.kine:kine-coroutine:1.3.8.0' //for Coroutine support
    compile 'com.kine:kine-gson:2.8.6.0' //for Gson support
    compile 'com.kine:kine-moshi:1.9.3' //for Moshi support
    compile 'com.kine:kine-okhttp:4.8.1.0' //for OkHttp support
    compile 'com.kine:kine-imageloader:1.0.0' //for Imageloading support
}
```

## Usage

Kine requests can be made with KineRequest class or using one of the `String` extension methods.
If you specify a callback the call is `asynchronous`, if you don't it's `synchronous`.

#### Asynchronous Example

```kotlin

     "https://jsonplaceholder.typicode.com/posts".httpGet().responseAs(JSONArray::class.java,{ response->
               val list =  Gson().fromJsonArray<Post>(response.response.toString())
           }, { e ->
               e.printStackTrace()
           })

     
```
#### Synchronous Example
```kotlin

val response =  "https://jsonplaceholder.typicode.com/posts".httpGet().responseAs(JSONArray::class.java)

```
