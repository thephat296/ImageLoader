package com.seagroup.seatalk.shopil.key

import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.request.ImageSource
import com.seagroup.seatalk.shopil.util.requireSize

class CacheKeyFactory {
    fun buildKey(request: ImageRequest): CacheKey? {
        val source = request.source
        val size = request.imageView.requireSize()
        val transformations = request.transformations?.mapNotNull {
            it.javaClass.canonicalName
        }
        return when (source) {
            is ImageSource.Drawable -> null
            is ImageSource.File -> CacheKey(source.data.path, size, transformations)
            is ImageSource.Uri -> CacheKey(source.data.toString(), size, transformations)
            is ImageSource.Url -> CacheKey(source.data, size, transformations)
        }
    }
}
