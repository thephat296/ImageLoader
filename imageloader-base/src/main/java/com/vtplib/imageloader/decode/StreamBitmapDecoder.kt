package com.vtplib.imageloader.decode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Size
import com.vtplib.imageloader.Result
import com.vtplib.imageloader.util.resize
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.internal.closeQuietly
import kotlin.coroutines.resume

internal class StreamBitmapDecoder : Decoder {
    override suspend fun decode(params: DecodeParams): Result<Bitmap> =
        suspendCancellableCoroutine { continuation ->
            val bitmapOption = BitmapFactory.Options()
            bitmapOption.inJustDecodeBounds = true
            BitmapFactory.decodeStream(params.source.peek().inputStream(), null, bitmapOption)
            val srcSize = Size(bitmapOption.outWidth, bitmapOption.outHeight)
            bitmapOption.inJustDecodeBounds = false

            bitmapOption.resize(srcSize, params.targetSize)
            val bitmap = params.source.use {
                BitmapFactory.decodeStream(it.inputStream(), null, bitmapOption)
            }
            if (bitmap != null) {
                continuation.resume(Result.Success(bitmap))
            } else {
                continuation.resume(Result.Error(IllegalArgumentException("Unsupported BufferedSource[$params]")))
            }

            continuation.invokeOnCancellation {
                params.source.closeQuietly()
            }
        }
}
