package com.seagroup.seatalk.shopil.cache

import android.graphics.Bitmap
import com.seagroup.seatalk.shopil.request.ImageRequest

interface CacheManager {
    suspend fun getImageFromCache(request: ImageRequest): Bitmap?
    suspend fun putImageToCache(request: ImageRequest, bitmap: Bitmap)
    fun clearMemoryCache()
    suspend fun clearDiskCache()
}
