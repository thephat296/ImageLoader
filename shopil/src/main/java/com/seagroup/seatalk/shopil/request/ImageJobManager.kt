package com.seagroup.seatalk.shopil.request

import android.view.View
import android.widget.ImageView
import kotlinx.coroutines.Job

class ImageJobManager(
    private val loadingJob: Job,
    imageView: ImageView
) : View.OnAttachStateChangeListener {

    init {
        imageView.addOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(v: View?) = Unit

    override fun onViewDetachedFromWindow(v: View?) {
        loadingJob.cancel()
    }
}
