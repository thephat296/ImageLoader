package com.vtplib.imageloader.util

import android.content.Context
import com.vtplib.imageloader.ImageLoader
import com.vtplib.imageloader.ImageLoaderFactory

val Context.imageLoader: ImageLoader
    get() = ImageLoaderFactory.get(this)
