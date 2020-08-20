package com.kine.imageloader.cache

import android.graphics.Bitmap
import java.util.*

/** caching class
 */
class MemoryCache  {
    private var maxSize = DEFAULT_CACHE_SIZE
    private var entries: HashMap<String, Bitmap> = HashMap()

     fun get(key: String): Bitmap? {
        if (entries.containsKey(key)) {
            return entries[key]
        }
        return null
    }

     fun put(key: String, cacheEntry: Bitmap) {

        synchronized(entries) { entries.put(key, cacheEntry) }
    }


     fun remove(key: String) {
        synchronized(entries) {
            if (entries.containsKey(key)) {
                entries.remove(key)
            }
        }
    }

     fun clear() {
        entries.clear()
    }

    private val currentSize: Int
        get() {
            return entries.size
        }

    companion object {
        private val TAG = MemoryCache::class.java.name

        /*
    * Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
    * and use 1/30th of the available memory for this memory cache.
    */
        val DEFAULT_CACHE_SIZE = (Runtime.getRuntime().maxMemory() / 30).toInt()
    }

}