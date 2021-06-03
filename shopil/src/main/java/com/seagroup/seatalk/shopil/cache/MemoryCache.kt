package com.seagroup.seatalk.shopil.cache

import android.graphics.Bitmap

interface MemoryCache {
    operator fun get(key: CacheKey): Bitmap?

    operator fun set(key: CacheKey, bitmap: Bitmap)

    fun remove(key: CacheKey): Boolean

    fun clear()
}
