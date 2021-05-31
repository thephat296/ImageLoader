package com.seagroup.seatalk.shopil

class CacheKeyFactory {
    fun buildKey(imageSource: ImageSource): CacheKey? = when (imageSource) {
        is ImageSource.Drawable -> null
        is ImageSource.File -> CacheKey(imageSource.data.path)
        is ImageSource.Uri -> CacheKey(imageSource.data.toString())
        is ImageSource.Url -> CacheKey(imageSource.data)
    }
}
