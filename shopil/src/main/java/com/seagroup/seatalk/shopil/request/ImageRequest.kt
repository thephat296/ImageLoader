package com.seagroup.seatalk.shopil.request

import android.widget.ImageView
import com.seagroup.seatalk.shopil.cache.CacheKey
import com.seagroup.seatalk.shopil.transform.Transformation
import com.seagroup.seatalk.shopil.util.requireSize

class ImageRequest(
    val source: ImageSource,
    val imageView: ImageView,
    val placeholder: ImageResource? = null,
    val error: ImageResource? = null,
    val transformations: List<Transformation>? = null
) {

    val cacheKey: CacheKey by lazy { buildKey() }

    private fun buildKey(): CacheKey {
        val size = imageView.requireSize()
        val transformations = transformations?.mapNotNull {
            it.javaClass.canonicalName
        }
        val sourceKey = when (source) {
            is ImageSource.File -> source.data.path
            is ImageSource.Uri -> source.data.toString()
            is ImageSource.Url -> source.data
        }
        return CacheKey(sourceKey, size, transformations)
    }

    class Builder(
        private val source: ImageSource,
        private val imageView: ImageView
    ) {

        private var placeholder: ImageResource? = null
        private var error: ImageResource? = null
        private var transformations: List<Transformation>? = null

        fun placeholder(placeholder: ImageResource) = apply {
            this.placeholder = placeholder
        }

        fun error(error: ImageResource) = apply {
            this.error = error
        }

        fun transformations(vararg transformations: Transformation) = apply {
            this.transformations = transformations.toList()
        }

        fun build() = ImageRequest(
            source = source,
            imageView = imageView,
            placeholder = placeholder,
            error = error,
            transformations = transformations
        )
    }
}
