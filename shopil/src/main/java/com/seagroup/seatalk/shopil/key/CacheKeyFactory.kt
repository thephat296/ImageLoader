package com.seagroup.seatalk.shopil.key

import com.seagroup.seatalk.shopil.request.ImageSource

class CacheKeyFactory {
    fun buildKey(imageSource: ImageSource): CacheKey? = when (imageSource) {
        is ImageSource.Drawable -> null
        is ImageSource.File -> CacheKey(imageSource.data.path)
        is ImageSource.Uri -> CacheKey(imageSource.data.toString())
        is ImageSource.Url -> CacheKey(imageSource.data)
    }
}
