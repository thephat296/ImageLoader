package com.seagroup.seatalk.shopil

import android.graphics.drawable.Drawable
import okio.BufferedSource

interface Decoder {
    suspend fun decode(source: BufferedSource): Drawable
}
