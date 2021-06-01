package com.seagroup.seatalk.shopil.key

import android.util.Size
import com.seagroup.seatalk.shopil.request.ImageSource

class CacheKeyFactory {
    fun buildKey(imageSource: ImageSource, size: Size): CacheKey? = when (imageSource) {
        is ImageSource.Drawable -> null
        is ImageSource.File -> CacheKey(imageSource.data.path, size)
        is ImageSource.Uri -> CacheKey(imageSource.data.toString(), size)
        is ImageSource.Url -> CacheKey(imageSource.data, size)
    }
}
