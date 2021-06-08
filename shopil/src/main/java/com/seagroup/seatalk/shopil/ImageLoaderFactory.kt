package com.seagroup.seatalk.shopil

import android.content.Context

object ImageLoaderFactory {
    private var imageLoader: ImageLoader? = null

    fun get(context: Context): ImageLoader = imageLoader ?: createImageLoader(context)

    @Synchronized
    private fun createImageLoader(context: Context): ImageLoader {
        imageLoader?.let { return it }

        val appContext = context.applicationContext
        return ((appContext as? Creator)?.createImageLoader() ?: ImageLoader.Builder(appContext).build())
            .also { imageLoader = it }
    }

    interface Creator {
        fun createImageLoader(): ImageLoader
    }
}
