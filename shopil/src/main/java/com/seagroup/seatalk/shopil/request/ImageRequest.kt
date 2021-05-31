package com.seagroup.seatalk.shopil.request

import android.widget.ImageView

class ImageRequest(
    val source: ImageSource,
    val imageView: ImageView,
    val placeholder: ImageResource? = null,
    val error: ImageResource? = null
) {

    class Builder(
        private val source: ImageSource,
        private val imageView: ImageView
    ) {

        private var placeholder: ImageResource? = null
        private var error: ImageResource? = null

        fun placeholder(placeholder: ImageResource) = apply {
            this.placeholder = placeholder
        }

        fun error(error: ImageResource) = apply {
            this.error = error
        }

        fun build() = ImageRequest(
            source = source,
            imageView = imageView,
            placeholder = placeholder,
            error = error
        )
    }
}
