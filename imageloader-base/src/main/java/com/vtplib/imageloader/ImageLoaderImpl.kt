package com.vtplib.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import com.vtplib.imageloader.cache.CacheManager
import com.vtplib.imageloader.decode.DecodeParams
import com.vtplib.imageloader.decode.Decoder
import com.vtplib.imageloader.fetch.Fetcher
import com.vtplib.imageloader.request.ImageRequest
import com.vtplib.imageloader.request.ImageSource
import com.vtplib.imageloader.transform.Transformer
import com.vtplib.imageloader.util.awaitViewToBeMeasured
import com.vtplib.imageloader.util.jobManager
import com.vtplib.imageloader.util.requireSize
import kotlinx.coroutines.*
import timber.log.Timber

internal class ImageLoaderImpl(
            private val appContext: Context,
    cacheManager: CacheManager,
    fetcher: Fetcher<ImageSource>,
    decoder: Decoder,
    transformer: Transformer
) : ImageLoader,
    CacheManager by cacheManager,
    Fetcher<ImageSource> by fetcher,
    Decoder by decoder,
    Transformer by transformer {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate +
            CoroutineExceptionHandler { _, throwable -> Timber.d(throwable) }
    )

    override fun enqueue(request: ImageRequest) {
        request.imageView.jobManager.cancelCurrentJob()

        fun setImage(drawable: Drawable?) = request.imageView.setImageDrawable(drawable)
        val job = scope.launch {
            request.imageView.awaitViewToBeMeasured()

            request.placeholder?.getDrawable(appContext)?.let(::setImage)
            withContext(Dispatchers.Default) {
                (getImageFromCache(request) ?: fetchImage(request))
                    ?.toDrawable(appContext.resources)
                    ?: request.error?.getDrawable(appContext)
            }.let(::setImage)
        }

        request.imageView.jobManager.setCurrentJob(job)
    }

    private suspend fun fetchImage(request: ImageRequest): Bitmap? =
        fetch(request.source)
            .flatMap { bufferedSource ->
                decode(DecodeParams(bufferedSource, request.imageView.requireSize()))
            }
            .map { bitmap ->
                transform(bitmap, request.transformations)
            }
            .doOnSuccess { bitmap ->
                putImageToCache(request, bitmap)
            }
            .doOnError(Timber::d)
            .data
}
