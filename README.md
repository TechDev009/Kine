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
- [x] Cancel any in-flight request
- [x] Request timeout with retry policy
- [x] AppWide Configuration using `Kime` for common Headers , base Url
- [x] Log manager with option to turn logging off for per request and App wide
- [x] Supports response deserialization into POJO/POKO with Gson or Moshi
- [x] Supports reactive programming via RxJava 2.x
- [x] Supports kotlin coroutines
- [x] Supports ability to handle any type of response with custom converter option

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


#### Get Json

```kotlin
"https://example/api/test".httpGet().responseAs(JSONObject::class.java,{ response->
               val response =  response.body
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://example/api/test".httpGet().responseAs(JSONObject::class.java)
```

#### Get String

```kotlin
"https://example/api/test".httpGet().responseAs(String::class.java,{ response->
               val response =  response.body
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://example/api/test".httpGet().responseAs(String::class.java)
```
#### Get JsonArray

```kotlin
"https://example/api/test".httpGet().responseAs(JSONArray::class.java,{ response->
               val list =  Gson().fromJsonArray<Post>(response.body.toString())
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://jsonplaceholder.typicode.com/posts".httpGet().responseAs(JSONArray::class.java)
```

#### Get Parsed Response With Gson(requires kine-gson dependency)/Moshi(requires kine-moshi dependency)

```kotlin
"https://example/api/test".httpGet().responseAs(User::class.java,{ response->
               val list =  Gson().fromJsonArray<Post>(response.body.toString())
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://example/api/test".httpGet().responseAs(User::class.java)
```

#### Use with RxJava2(requires kine-rxjava2 dependency)

#### Single

```kotlin
"https://example/api/test".httpGet().toSingle(clazz)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<KineResponse<T>?> {
                override fun onSubscribe(d: Disposable) {
                }
                override fun onSuccess(response: KineResponse<T>) {
                    val response = response.body.toString()
                }
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
```

#### Flowable

```kotlin
"https://example/api/test".httpGet().toFlowable(clazz)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : FlowableSubscriber<KineResponse<T>> {

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
                override fun onSubscribe(s: Subscription) {
                    s.request(1)
                }
                override fun onNext(response: KineResponse<T>) {
                    val response = response.body.toString()
                }
                override fun onComplete() {
                }
            })
```

#### Observable

```kotlin
 "https://example/api/test".httpGet().toObservable(clazz)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<KineResponse<T>?> {
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
                override fun onNext(response: KineResponse<T>) {
                    val response = response.body.toString()
                }
                override fun onComplete() {
                }
                override fun onSubscribe(d: Disposable) {
                }
            })
```

#### Use with Coroutine(requires kine-coroutine dependency)

```kotlin
 GlobalScope.launch(Dispatchers.Main) {
            val response = "https://example/api/test".httpGet().responseAsCoroutine(clazz)
        }
```

#### Download File

```kotlin
"https://example/api/test/files/test10Mb.db".downloadTo(
            File(Environment.getExternalStorageDirectory(),"test.db"),{downloaded,total->
// note the progress listener is not called on main thread it is always called on background thread for async request and calling thread on sync 
// request
                activity?.runOnUiThread {
                    val progress = "progress ${((downloaded*100)/total)}"
                }
            }, { response ->
                val savedPath = response.body?.path
            }, { e ->
                e.printStackTrace()
            })
```

#### Image Loading(requires kine-imageloader dependency)

#### Load Bitmap From Url
```kotlin
  "https://example/api/test/files/abc.png".loadBitmapResponseFromUrl( { response ->
            imageView!!.setImageBitmap(response.body)
        }, { e -> e.printStackTrace() })
```

#### Load Image from Url to ImageView
```kotlin
  imageView.loadImage("https://example/api/test/files/abc.png",placeHolderResId)
```
