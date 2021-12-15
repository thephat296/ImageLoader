package com.vtplib.imageloader.transform

import android.graphics.Bitmap

interface Transformation {
    suspend fun transform(input: Bitmap): Bitmap
}
