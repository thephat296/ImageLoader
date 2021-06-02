package com.seagroup.seatalk.shopil.decode

import android.graphics.drawable.Drawable
import com.seagroup.seatalk.shopil.request.ImageResult

sealed class DecodeResult {
    class Success(val drawable: Drawable) : DecodeResult()
    class Error(val throwable: Throwable) : DecodeResult()

    fun mapToImageResult(): ImageResult = when (this) {
        is Error -> ImageResult.Error(throwable)
        is Success -> ImageResult.Success(drawable)
    }
}
