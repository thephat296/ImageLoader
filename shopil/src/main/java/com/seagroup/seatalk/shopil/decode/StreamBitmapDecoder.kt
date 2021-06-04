package com.seagroup.seatalk.shopil.decode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Size
import com.seagroup.seatalk.shopil.Result
import com.seagroup.seatalk.shopil.util.resize

internal class StreamBitmapDecoder : Decoder {
    override suspend fun decode(params: DecodeParams): Result<Bitmap> {
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inJustDecodeBounds = true
        BitmapFactory.decodeStream(params.source.peek().inputStream(), null, bitmapOption)
        val srcSize = Size(bitmapOption.outWidth, bitmapOption.outHeight)
        bitmapOption.inJustDecodeBounds = false

        bitmapOption.resize(srcSize, params.targetSize)
        val bitmap = params.source.use {
            BitmapFactory.decodeStream(it.inputStream(), null, bitmapOption)
        } ?: return Result.Error(IllegalArgumentException("Unsupported BufferedSource[$params]"))
        return Result.Success(bitmap)
    }
}
