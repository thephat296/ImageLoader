package com.seagroup.seatalk.shopil.decoder

import android.util.Size
import okio.BufferedSource

data class DecodeParams(
    val source: BufferedSource,
    val targetSize: Size
)
