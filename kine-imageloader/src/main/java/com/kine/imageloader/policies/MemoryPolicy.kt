package com.kine.imageloader.policies

/** Designates the policy to use when dealing with memory cache.  */
enum class MemoryPolicy(val index: Int) {
    /** Skips memory cache lookup when processing a request.  */
    CACHE(1 shl 0),

    /**
     * Skips storing the final result into memory cache. Useful for one-off requests
     * to avoid evicting other bitmaps from the cache.
     */
    STORE(1 shl 1);

    companion object {
        @JvmStatic
        fun shouldReadFromMemoryCache(memoryPolicy: Int): Boolean {
            return memoryPolicy and CACHE.index != 0
        }

        @JvmStatic
        fun shouldWriteToMemoryCache(memoryPolicy: Int): Boolean {
            return memoryPolicy and STORE.index != 0
        }
    }

}