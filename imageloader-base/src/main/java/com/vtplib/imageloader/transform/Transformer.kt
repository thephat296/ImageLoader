package com.vtplib.imageloader.transform

import android.graphics.Bitmap

internal interface Transformer {
    suspend fun transform(input: Bitmap, transformations: List<Transformation>?): Bitmap
}
