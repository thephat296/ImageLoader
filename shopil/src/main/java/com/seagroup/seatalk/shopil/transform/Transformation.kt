package com.seagroup.seatalk.shopil.transform

import android.graphics.Bitmap

interface Transformation {
    suspend fun transform(input: Bitmap): Bitmap
}
