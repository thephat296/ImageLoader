package com.seagroup.seatalk.shopil

import android.graphics.Bitmap
import androidx.collection.LruCache

class DefaultMemoryCache(maxSize: Int) : MemoryCache {
    private val cache = LruCache<CacheKey, Bitmap>(maxSize)

    override fun get(key: CacheKey): Bitmap? = cache.get(key)

    override fun set(key: CacheKey, bitmap: Bitmap) {
        val size = bitmap.allocationByteCount
        if (size > cache.maxSize()) {
            cache.remove(key) // evict an existing element with the same key if it exists.
        } else {
            cache.put(key, bitmap)
        }
    }

    override fun remove(key: CacheKey): Boolean = cache.remove(key) != null

    override fun clear() = cache.evictAll()
}
