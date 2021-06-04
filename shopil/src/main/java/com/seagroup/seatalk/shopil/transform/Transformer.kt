package com.seagroup.seatalk.shopil.transform

import android.graphics.Bitmap

internal interface Transformer {
    suspend fun transform(input: Bitmap, transformations: List<Transformation>?): Bitmap
}
