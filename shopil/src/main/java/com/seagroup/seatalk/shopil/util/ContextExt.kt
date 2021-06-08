package com.seagroup.seatalk.shopil.util

import android.content.Context
import com.seagroup.seatalk.shopil.ImageLoader
import com.seagroup.seatalk.shopil.ImageLoaderFactory

val Context.imageLoader: ImageLoader
    get() = ImageLoaderFactory.get(this)
