package com.seagroup.seatalk.shopil

import android.widget.ImageView

fun ImageView.load(url: String, builder: ImageRequest.Builder.() -> Unit = {}) {
    val request = ImageRequest.Builder(imgUrl = url, imageView = this)
        .apply(builder)
        .build()
    (context.applicationContext as AndroidApplication).imageLoader.enqueue(request)
}
