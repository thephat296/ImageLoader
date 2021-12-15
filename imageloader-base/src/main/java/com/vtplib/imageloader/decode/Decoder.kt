package com.vtplib.imageloader.decode

import android.graphics.Bitmap
import com.vtplib.imageloader.Result

internal interface Decoder {
    suspend fun decode(params: DecodeParams): Result<Bitmap>
}
