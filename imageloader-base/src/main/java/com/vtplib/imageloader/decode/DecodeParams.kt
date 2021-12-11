package com.vtplib.imageloader.decode

import android.util.Size
import okio.BufferedSource

internal data class DecodeParams(
    val source: BufferedSource,
    val targetSize: Size
)
