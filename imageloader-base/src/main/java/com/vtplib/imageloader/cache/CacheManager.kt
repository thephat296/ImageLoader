package com.vtplib.imageloader.cache

import android.graphics.Bitmap
import com.vtplib.imageloader.request.ImageRequest

interface CacheManager {
    suspend fun getImageFromCache(request: ImageRequest): Bitmap?
    suspend fun putImageToCache(request: ImageRequest, bitmap: Bitmap)
    fun clearMemoryCache()
    suspend fun clearDiskCache()
}
