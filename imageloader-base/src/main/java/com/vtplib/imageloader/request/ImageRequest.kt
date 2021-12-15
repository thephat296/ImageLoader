package com.vtplib.imageloader.request

import android.widget.ImageView
import com.vtplib.imageloader.cache.CacheKey
import com.vtplib.imageloader.cache.CachePolicy
import com.vtplib.imageloader.transform.Transformation
import com.vtplib.imageloader.util.requireSize

class ImageRequest(
    val source: ImageSource,
    val imageView: ImageView,
    val placeholder: ImageResource? = null,
    val error: ImageResource? = null,
    val transformations: List<Transformation>? = null,
    val memoryCachePolicy: CachePolicy,
    val diskCachePolicy: CachePolicy
) {

    val cacheKey: CacheKey by lazy(::buildKey)

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
        private var memoryCachePolicy: CachePolicy = CachePolicy.ENABLED
        private var diskCachePolicy: CachePolicy = CachePolicy.ENABLED

        fun placeholder(placeholder: ImageResource) = apply {
            this.placeholder = placeholder
        }

        fun error(error: ImageResource) = apply {
            this.error = error
        }

        fun transformations(vararg transformations: Transformation) = apply {
            this.transformations = transformations.toList()
        }

        fun memoryCachePolicy(cachePolicy: CachePolicy) = apply {
            this.memoryCachePolicy = cachePolicy
        }

        fun diskCachePolicy(cachePolicy: CachePolicy) = apply {
            this.diskCachePolicy = cachePolicy
        }

        fun build() = ImageRequest(
            source = source,
            imageView = imageView,
            placeholder = placeholder,
            error = error,
            transformations = transformations,
            memoryCachePolicy = memoryCachePolicy,
            diskCachePolicy = diskCachePolicy
        )
    }
}
