package com.seagroup.seatalk.shopil

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import okio.BufferedSource

internal class BitmapDecoder(private val context: Context) : Decoder {

    override suspend fun decode(source: BufferedSource): Drawable =
        source.use {
            BitmapFactory.decodeStream(it.inputStream())
        }
            .toDrawable(context.resources)
}
