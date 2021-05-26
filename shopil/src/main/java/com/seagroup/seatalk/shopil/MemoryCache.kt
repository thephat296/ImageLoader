package com.seagroup.seatalk.shopil

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface MemoryCache {
    operator fun get(key: Key): Bitmap?

    operator fun set(key: Key, bitmap: Bitmap)

    fun remove(key: Key): Boolean

    fun clear()

    @Parcelize
    data class Key(val base: String) : Parcelable
}
