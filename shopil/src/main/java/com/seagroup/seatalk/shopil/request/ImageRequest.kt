package com.seagroup.seatalk.shopil.request

import android.widget.ImageView
import com.seagroup.seatalk.shopil.transform.Transformation

class ImageRequest(
    val source: ImageSource,
    val imageView: ImageView,
    val placeholder: ImageResource? = null,
    val error: ImageResource? = null,
    val transformations: List<Transformation>? = null
) {

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
