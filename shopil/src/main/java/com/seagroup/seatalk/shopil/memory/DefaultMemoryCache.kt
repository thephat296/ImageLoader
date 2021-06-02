package com.seagroup.seatalk.shopil.memory

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.seagroup.seatalk.shopil.key.CacheKey

internal class DefaultMemoryCache(maxSize: Int) : MemoryCache {
    private val cache = object : LruCache<CacheKey, Bitmap>(maxSize) {
        override fun sizeOf(key: CacheKey, value: Bitmap) = value.allocationByteCount
    }

    override fun get(key: CacheKey): Bitmap? = cache.get(key)

    override fun set(key: CacheKey, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }

    override fun remove(key: CacheKey): Boolean = cache.remove(key) != null

    override fun clear() = cache.evictAll()
}
