package com.seagroup.seatalk.shopil.request

sealed class ImageSource {
    data class Url(val data: String) : ImageSource()
    data class Uri(val data: android.net.Uri) : ImageSource()
    data class File(val data: java.io.File) : ImageSource()
}
