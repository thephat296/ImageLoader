package com.seagroup.seatalk.shopil

import android.graphics.drawable.Drawable

sealed class ImageResult {
    class Success(val drawable: Drawable) : ImageResult()
    class Error(val throwable: Throwable) : ImageResult()

    fun doOnSuccess(function: (Drawable) -> Unit): ImageResult {
        if (this is Success) {
            function(drawable)
        }
        return this
    }
}
