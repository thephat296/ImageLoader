package com.seagroup.seatalk.shopil.cache

import android.graphics.Bitmap
import com.seagroup.seatalk.shopil.CacheManager
import timber.log.Timber

class CacheManagerImpl(
    private val memoryCache: MemoryCache,
    private val diskCache: DiskCache
) : CacheManager {
    override suspend fun getCache(key: CacheKey): Bitmap? =
        memoryCache[key]
            ?.also { Timber.d("[ImageLoader] get value from memory cache") }
            ?: diskCache.get(key)
                ?.also { bitmap ->
                    memoryCache[key] = bitmap
                    Timber.d("[ImageLoader] get value from disk cache and put to memory cache")
                }

    override suspend fun putCache(key: CacheKey, bitmap: Bitmap) {
        diskCache.put(key, bitmap)
            .also { Timber.d("[ImageLoader] put value to disk cache") }
        memoryCache[key] = bitmap
            .also { Timber.d("[ImageLoader] put value to memory cache") }
    }

    override fun clearMemoryCache() = memoryCache.clear()

    override suspend fun clearDiskCache() = diskCache.clear()
}
