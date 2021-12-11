package com.vtplib.imageloader.request

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources

sealed class ImageResource {
    class Drawable(val drawable: android.graphics.drawable.Drawable) : ImageResource()
    class DrawableRes(@androidx.annotation.DrawableRes val resId: Int) : ImageResource()

    fun getDrawable(context: Context): android.graphics.drawable.Drawable? = when (this) {
        is Drawable -> drawable
        is DrawableRes -> AppCompatResources.getDrawable(context, resId)
    }
}
