package com.seagroup.seatalk.shopil.util

import android.util.Size
import kotlin.math.max

object BitmapUtils {
    fun calculateInSampleSize(srcSize: Size, dstSize: Size): Int {
        val widthInSampleSize = Integer.highestOneBit(srcSize.width / dstSize.width).coerceAtLeast(1)
        val heightInSampleSize = Integer.highestOneBit(srcSize.height / dstSize.height).coerceAtLeast(1)
        return max(widthInSampleSize, heightInSampleSize)
    }
}
