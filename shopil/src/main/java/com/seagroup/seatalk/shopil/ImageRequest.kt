package com.seagroup.seatalk.shopil

import android.widget.ImageView

class ImageRequest(
    val imgUrl: String,
    val imageView: ImageView,
    val placeholder: ImageResource? = null,
    val error: ImageResource? = null
) {

    class Builder(
        private val imgUrl: String,
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
            imgUrl = imgUrl,
            imageView = imageView,
            placeholder = placeholder,
            error = error
        )
    }
}
