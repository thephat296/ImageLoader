package com.seagroup.seatalk.shopil.util

import android.graphics.BitmapFactory
import android.util.Size
import kotlin.math.roundToInt

internal fun BitmapFactory.Options.resize(srcSize: Size, dstSize: Size) {
    inSampleSize = BitmapUtils.calculateInSampleSize(srcSize, dstSize)
    val scale = BitmapUtils
        .calculateSizeScale(srcSize = srcSize.scaleDownTo(inSampleSize), dstSize = dstSize)
        .takeIf { it < 1.0 } // Avoid loading the image larger than its original dimensions
        ?: return
    inScaled = true
    inDensity = srcSize.width
    inTargetDensity = (inDensity * scale).roundToInt()
}
