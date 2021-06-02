package com.seagroup.seatalk.shopil.util

import android.util.Size
import android.view.ViewTreeObserver
import android.widget.ImageView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun ImageView.awaitSize(): Size {
    // the view is already measured.
    getSize()?.let { return it }

    // wait for the view to be measured.
    return suspendCancellableCoroutine { continuation ->

        val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
            private var isResumed = false

            override fun onPreDraw(): Boolean {
                val size = getSize() ?: return true
                viewTreeObserver.removeOnPreDrawListener(this)
                if (!isResumed) {
                    isResumed = true
                    continuation.resume(size)
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

fun ImageView.requireSize(): Size = getSize()!!

private fun ImageView.getSize(): Size? =
    Size(width, height)
        .takeIf {
            it.width > 0 && it.height > 0
        }
