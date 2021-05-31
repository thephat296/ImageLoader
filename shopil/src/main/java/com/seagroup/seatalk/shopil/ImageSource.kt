package com.seagroup.seatalk.shopil

sealed class ImageSource {
    class Url(val data: String) : ImageSource()
    class Uri(val data: android.net.Uri) : ImageSource()
    class File(val data: java.io.File) : ImageSource()
    class Drawable(val data: android.graphics.drawable.Drawable) : ImageSource()
}
