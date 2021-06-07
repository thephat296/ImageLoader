package com.seagroup.seatalk.shopil.cache

import android.graphics.Bitmap
import com.seagroup.seatalk.shopil.request.ImageRequest
import timber.log.Timber

internal class CacheManagerImpl(
    private val memoryCache: MemoryCache,
    private val diskCache: DiskCache
) : CacheManager {
    override suspend fun getCache(request: ImageRequest): Bitmap? {
        val key = request.cacheKey
        return invokeWhen(request.memoryCachePolicy.enabled) { memoryCache[key] }
            ?.also { Timber.d("[ImageLoader] get value from memory cache") }
            ?: invokeWhen(request.diskCachePolicy.enabled) { diskCache.get(key) }
                ?.also { bitmap ->
                    invokeWhen(request.memoryCachePolicy.enabled) { memoryCache[key] = bitmap }
                    Timber.d("[ImageLoader] get value from disk cache and put to memory cache")
                }
    }

    private suspend fun <T> invokeWhen(predicate: Boolean, func: suspend () -> T?): T? =
        if (predicate) func() else null

    override suspend fun putCache(request: ImageRequest, bitmap: Bitmap) {
        val key = request.cacheKey
        if (request.diskCachePolicy.enabled) {
            diskCache.put(key, bitmap)
                .also { Timber.d("[ImageLoader] put value to disk cache") }
        }
        if (request.memoryCachePolicy.enabled) {
            memoryCache[key] = bitmap
                .also { Timber.d("[ImageLoader] put value to memory cache") }
        }
    }

    override fun clearMemoryCache() = memoryCache.clear()

    override suspend fun clearDiskCache() = diskCache.clear()
}
