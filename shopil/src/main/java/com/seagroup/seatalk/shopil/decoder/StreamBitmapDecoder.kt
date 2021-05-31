package com.seagroup.seatalk.shopil.decoder

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toDrawable
import okio.BufferedSource

internal class StreamBitmapDecoder(private val context: Context) : Decoder {
    override suspend fun decode(source: BufferedSource): DecodeResult {
        val bitmap = source.use {
            BitmapFactory.decodeStream(it.inputStream())
        } ?: return DecodeResult.Error(IllegalArgumentException("Unsupported BufferedSource[$source]"))
        return DecodeResult.Success(drawable = bitmap.toDrawable(context.resources))
    }
}
