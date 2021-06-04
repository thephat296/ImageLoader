package com.seagroup.seatalk.shopil.cache

import android.graphics.Bitmap

interface CacheManager {
    suspend fun getCache(key: CacheKey): Bitmap?
    suspend fun putCache(key: CacheKey, bitmap: Bitmap)
    fun clearMemoryCache()
    suspend fun clearDiskCache()
}
