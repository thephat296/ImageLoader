package com.seagroup.seatalk.shopil.memory

import android.graphics.Bitmap
import com.seagroup.seatalk.shopil.key.CacheKey

interface MemoryCache {
    operator fun get(key: CacheKey): Bitmap?

    operator fun set(key: CacheKey, bitmap: Bitmap)

    fun remove(key: CacheKey): Boolean

    fun clear()
}
