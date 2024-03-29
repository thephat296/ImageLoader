package com.vtplib.imageloader.util

import android.content.res.Resources
import android.util.Size
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.vtp.imageloader.R
import com.vtplib.imageloader.request.ImageJobManager
import com.vtplib.imageloader.request.ImageRequest
import com.vtplib.imageloader.request.ImageSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

fun ImageView.load(url: String, builder: ImageRequest.Builder.() -> Unit = {}) {
    val request = ImageRequest.Builder(source = ImageSource.Url(url), imageView = this)
        .apply(builder)
        .build()
    context.imageLoader.enqueue(request)
}

internal val ImageView.jobManager: ImageJobManager
    get() {
        (getTag(R.id.image_job_manager) as? ImageJobManager)
            ?.let { return it }
        return ImageJobManager().apply {
            addOnAttachStateChangeListener(this)
            setTag(R.id.image_job_manager, this)
        }
    }

internal suspend fun ImageView.awaitViewToBeMeasured() {
    // the view is already measured.
    getSize()?.let { return }

    // wait for the view to be measured.
    return suspendCancellableCoroutine { continuation ->

        val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
            private var isResumed = false

            override fun onPreDraw(): Boolean {
                getSize() ?: return true
                viewTreeObserver.removeOnPreDrawListener(this)
                if (!isResumed) {
                    isResumed = true
                    continuation.resume(Unit)
                }
                return true
            }
        }

        viewTreeObserver.addOnPreDrawListener(preDrawListener)

        continuation.invokeOnCancellation {
            viewTreeObserver.removeOnPreDrawListener(preDrawListener)
        }
    }
}

internal fun ImageView.requireSize(): Size = getSize()!!

private fun ImageView.getSize(): Size? {
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    val viewWidth = if (layoutParams.width == WRAP_CONTENT) screenWidth else width
    val viewHeight = if (layoutParams.height == WRAP_CONTENT) screenHeight else height
    return Size(viewWidth, viewHeight)
        .takeIf {
            it.width > 0 && it.height > 0
        }
}
