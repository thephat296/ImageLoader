package com.seagroup.seatalk.shopil.decoder

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Size
import androidx.core.graphics.drawable.toDrawable
import com.seagroup.seatalk.shopil.util.resize

internal class StreamBitmapDecoder(private val context: Context) : Decoder {
    override suspend fun decode(params: DecodeParams): DecodeResult {
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inJustDecodeBounds = true
        BitmapFactory.decodeStream(params.source.peek().inputStream(), null, bitmapOption)
        val srcSize = Size(bitmapOption.outWidth, bitmapOption.outHeight)
        bitmapOption.inJustDecodeBounds = false

        bitmapOption.resize(srcSize, params.targetSize)
        val bitmap = params.source.use {
            BitmapFactory.decodeStream(it.inputStream(), null, bitmapOption)
        } ?: return DecodeResult.Error(IllegalArgumentException("Unsupported BufferedSource[$params]"))
        return DecodeResult.Success(drawable = bitmap.toDrawable(context.resources))
    }
}
