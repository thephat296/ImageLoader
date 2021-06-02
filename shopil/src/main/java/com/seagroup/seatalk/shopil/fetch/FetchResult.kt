package com.seagroup.seatalk.shopil.fetch

import okio.BufferedSource

sealed class FetchResult {
    data class Source(val source: BufferedSource) : FetchResult()
    data class Drawable(val drawable: android.graphics.drawable.Drawable) : FetchResult()
    data class Error(val exception: Exception) : FetchResult()
}
