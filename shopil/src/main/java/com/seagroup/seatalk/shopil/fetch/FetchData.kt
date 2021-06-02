package com.seagroup.seatalk.shopil.fetch

import okio.BufferedSource

sealed class FetchData {
    data class Source(val source: BufferedSource) : FetchData()
    data class Drawable(val drawable: android.graphics.drawable.Drawable) : FetchData()
}
