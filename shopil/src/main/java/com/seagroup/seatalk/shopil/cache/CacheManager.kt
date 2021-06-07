package com.seagroup.seatalk.shopil.cache

import android.graphics.Bitmap
import com.seagroup.seatalk.shopil.request.ImageRequest

interface CacheManager {
    suspend fun getCache(request: ImageRequest): Bitmap?
    suspend fun putCache(request: ImageRequest, bitmap: Bitmap)
    fun clearMemoryCache()
    suspend fun clearDiskCache()
}
