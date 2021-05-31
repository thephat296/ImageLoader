package com.seagroup.seatalk.shopil

import android.widget.ImageView

fun ImageView.load(source: ImageSource, builder: ImageRequest.Builder.() -> Unit = {}) {
    val request = ImageRequest.Builder(source = source, imageView = this)
        .apply(builder)
        .build()
    (context.applicationContext as AndroidApplication).imageLoader.enqueue(request)
}
