package com.seagroup.seatalk.shopil.util

import android.util.Size
import android.view.ViewTreeObserver
import android.widget.ImageView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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

private fun ImageView.getSize(): Size? =
    Size(width, height)
        .takeIf {
            it.width > 0 && it.height > 0
        }
