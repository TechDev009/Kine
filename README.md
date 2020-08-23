![Kine](logo.png)

# Kine (Kotlin I/O Networking Extensions)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.0-blue.svg)](https://kotlinlang.org) 
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/AnkitAgrawal967/Kine/blob/master/LICENSE.md)

A simple and powerful HTTP networking library for Kotlin/Android.

## Features

- [x] Supports HTTP GET/POST/PUT/DELETE/HEAD/PATCH
- [x] Supports synchronous and asynchronous requests
- [x] Download file with progress
- [x] Get Image as bitmap from url
- [x] MultiPart Upload
- [x] Response caching in disk 
- [x] OkHttp cache control full support
- [x] Supports OkHttp with ability to write your own Httpclient
- [x] Cancel any in-flight request
- [x] Request timeout with retry policy
- [x] App Wide Configuration using `Kime` with support for common Headers , base Url for all request
- [x] Log manager with option to turn logging off for per request and App wide
- [x] Supports response deserialization into POJO/POKO with Gson or Moshi
- [x] Supports reactive programming via RxJava 2.x / RxJava 3.x
- [x] Supports kotlin coroutines
- [x] Supports live data
- [x] Supports ability to handle any type of response with custom converter option

## Upcoming Planned Features

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
    compile 'com.kine:kine:1.0.0.0' //for JVM
    compile 'com.kine:kine-json:2.8.6.0' //for json support(do not use this dependency in android project)
    compile 'com.kine:kine-android:1.0.0.0' //for Android
    compile 'com.kine:kine-rxjava2:2.2.19.0' //for RxJava2 support
    compile 'com.kine:kine-rxjava3:3.0.6.0' //for RxJava3 support
    compile 'com.kine:kine-coroutine:1.3.8.0' //for Coroutine support
    compile 'com.kine:kine-livedata:1.3.8.0' //for LiveData support
    compile 'com.kine:kine-gson:2.8.6.0' //for Gson support
    compile 'com.kine:kine-moshi:1.9.3.0' //for Moshi support
    compile 'com.kine:kine-okhttp:4.8.1.0' //for OkHttp support
    compile 'com.kine:kine-okhttplegacy:3.12.12.0' //for Legacy OkHttp support(API 9)
    compile 'com.kine:kine-imageloader:1.0.0.0' //for Imageloading support
}
```

##### Note: Kine will autodetect if android,okhttp,gson or moshi dependency are there and set appropriate client, converter or main thread callback you don't need to specify them,if both moshi and gson are there, kine will add moshi first(converter are always called by add order so if you have both dependency and trying to parse it using gson it won't work). To override this behaviour you can set them using `Kine.builder()` as shown below

## Usage

### Kine Configuration

```kotlin
Kine.Builder()
            .headers(hashMapOf()) // common headers for all app requests
            .client(OkHttpKineClient()) // common client to use for all requests
            .converter(GsonConverter()) // gson converter for parsing response
            .connectionChecker(SimpleConnectionChecker(context!!)) // a simple connection checker for no internet
            .baseUrl(ConfigUtils.dummyBaseUrl) // a base url for all requests
            .logLevel(LogLevel.ERROR) // logs to display according to level
            .disableAllLogs(true) // disable all logging for all requests
            .build()
```

##### Note: Options provided with individual `KineRequest` will always take priority over `Kine` global configuration except for headers , headers will always be added to individual request headers specified by the user


Kine requests can be made with `KineRequest` class or using one of the `String` or other extension methods.
If you specify a callback the call is asynchronous, if you don't its synchronous.
Exception will be thrown in synchronous (you need to catch them).
Exception will be delivered to onError callback in asynchronous except for user error like not specifying a converter and expecting a parsed response.


### Get Json

```kotlin
"https://example/api/test".httpGet().responseAs(JSONObject::class.java,{ response->
               val response =  response.body
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://example/api/test".httpGet().responseAs(JSONObject::class.java)
```

### Get String

```kotlin
"https://example/api/test".httpGet().responseAs(String::class.java,{ response->
               val response =  response.body
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://example/api/test".httpGet().responseAs(String::class.java)
```
### Get JsonArray

```kotlin
"https://example/api/test".httpGet().responseAs(JSONArray::class.java,{ response->
               val list =  Gson().fromJsonArray<Post>(response.body.toString())
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://jsonplaceholder.typicode.com/posts".httpGet().responseAs(JSONArray::class.java)
```

### Gson (requires kine-gson dependency) / Moshi (requires kine-moshi dependency)

```kotlin
"https://example/api/test".httpGet().responseAs(User::class.java,{ response->
               val user:User =  response.body
           }, { e ->
               e.printStackTrace()
           })
// for sync
val response =  "https://example/api/test".httpGet().responseAs(User::class.java)
```

### RxJava2 (requires kine-rxjava2 dependency) / RxJava3 (requires kine-rxjava3 dependency)

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

### Kotlin Coroutine (requires kine-coroutine dependency)

```kotlin
 GlobalScope.launch(Dispatchers.Main) {
            val response = "https://example/api/test".httpGet().responseAsCoroutine(clazz)
        }
```

### Download File

```kotlin
"https://example/api/test/files/test10Mb.db".downloadTo(
            File(Environment.getExternalStorageDirectory(),"test.db"),{downloaded,total->
// note the progress listener is not called on main thread it is always called on background thread 
// for async request and calling thread on sync request
                activity?.runOnUiThread {
                    val progress = "progress ${((downloaded*100)/total)}"
                }
            }, { response ->
                val savedPath = response.body?.path
            }, { e ->
                e.printStackTrace()
            })
```

### Image Loading (requires kine-imageloader dependency)

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

### Cancelling request

#### Application
```kotlin
 Kine.cancelAllRequests() // cancels all request with clients set with Kine
 Kine.cancelAllRequests(tag) // cancels all request with that tag with clients 
 // set with Kine(note passing null will cancel all request)
```

#### Individual Request
```kotlin
  val client = OkHttpKineClient()
  "url".httpGet().client(client).responseAs(User::class.java)
  client.cancelAllRequests() // behaves same as above Kine cancel methods
```

### MultiPart Image Upload

```kotlin
   KineRequest.upload("").addMultiPartParam("name","test",null).addMultiPartFileParam("tset",
   File("sdcard/test.apk"),null).setUploadListener { bytesWritten, total ->  }.responseAs(User::class.java,{ response->
   val response = response.body
     },{e->
           e.printStackTrace()
     })
```


## Requirements

### For Android

OkHttp 4x
- Min SDK 21+(for OkHttpClient you can write your own client for supporting Api Version below it)
- Java 8+

OkHttp 3x (legacy)
- Min SDK 9+(for OkHttpClient you can write your own client for supporting Api Version below it)
- Java 7+

### For Kotlin/Java

- Kotlin 1.4/Java 8+

## R8 / Proguard

If you use R8/Proguard , you may need to add rules for 
[Coroutines](https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/jvm/resources/META-INF/proguard/coroutines.pro), 
[RxJava2](https://github.com/ReactiveX/RxJava/blob/2.x/src/main/resources/META-INF/proguard/rxjava2.pro)
[RxJava3](https://github.com/ReactiveX/RxJava/blob/3.x/src/main/resources/META-INF/proguard/rxjava3.pro)
[OkHttp](https://github.com/square/okhttp/blob/master/okhttp/src/main/resources/META-INF/proguard/okhttp3.pro) and
 [Okio](https://github.com/square/okio/blob/master/okio/src/jvmMain/resources/META-INF/proguard/okio.pro).
 
## License

    Copyright 2020 Kine

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
