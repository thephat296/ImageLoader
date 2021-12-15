package com.vtplib.imageloader.util

import android.util.Size
import kotlin.math.max

internal object BitmapUtils {
    fun calculateInSampleSize(srcSize: Size, dstSize: Size): Int {
        val widthInSampleSize = Integer.highestOneBit(srcSize.width / dstSize.width).coerceAtLeast(1)
        val heightInSampleSize = Integer.highestOneBit(srcSize.height / dstSize.height).coerceAtLeast(1)
        return max(widthInSampleSize, heightInSampleSize)
    }

    fun calculateSizeScale(srcSize: Size, dstSize: Size): Double {
        val widthPercent = dstSize.width.toDouble() / srcSize.width
        val heightPercent = dstSize.height.toDouble() / srcSize.height
        return max(widthPercent, heightPercent)
    }
}
