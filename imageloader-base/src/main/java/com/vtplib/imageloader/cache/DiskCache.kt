package com.vtplib.imageloader.cache

import android.graphics.Bitmap

interface DiskCache {
    suspend fun put(key: CacheKey, bitmap: Bitmap)
    suspend fun get(key: CacheKey): Bitmap?
    suspend fun delete(key: CacheKey): Boolean
    suspend fun clear()
}
