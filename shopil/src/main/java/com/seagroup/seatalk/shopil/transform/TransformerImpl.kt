package com.seagroup.seatalk.shopil.transform

import android.graphics.Bitmap

internal class TransformerImpl : Transformer {
    override suspend fun transform(input: Bitmap, transformations: List<Transformation>?): Bitmap =
        transformations?.fold(input) { bitmap, transformation ->
            transformation.transform(bitmap)
        } ?: input
}
