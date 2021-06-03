package com.seagroup.seatalk.shopil

import android.graphics.Bitmap
import com.seagroup.seatalk.shopil.cache.CacheKey

interface CacheManager {
    suspend fun getCache(key: CacheKey): Bitmap?
    suspend fun putCache(key: CacheKey, bitmap: Bitmap)
    fun clearMemoryCache()
    suspend fun clearDiskCache()
}
