package com.seagroup.seatalk.shopil

import android.widget.ImageView

fun ImageView.load(url: String) {
    val request = ImageRequest(imgUrl = url, imageView = this)
    (context.applicationContext as AndroidApplication).imageLoader.enqueue(request)
}
