package com.seagroup.seatalk.shopil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import com.seagroup.seatalk.shopil.cache.CacheManager
import com.seagroup.seatalk.shopil.decode.DecodeParams
import com.seagroup.seatalk.shopil.decode.Decoder
import com.seagroup.seatalk.shopil.fetch.Fetcher
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.request.ImageSource
import com.seagroup.seatalk.shopil.transform.Transformer
import com.seagroup.seatalk.shopil.util.awaitViewToBeMeasured
import com.seagroup.seatalk.shopil.util.requireSize
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.coroutineContext

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
        fun setImage(drawable: Drawable?) = request.imageView.setImageDrawable(drawable)
        scope.launch {
            request.imageView.awaitViewToBeMeasured()

            request.placeholder?.getDrawable(appContext)?.let(::setImage)
            withContext(Dispatchers.IO) {
                (getCache(request.cacheKey) ?: fetchImage(request))
                    ?.toDrawable(appContext.resources)
                    ?: request.error?.getDrawable(appContext)
            }.let(::setImage)
        }
    }

    private suspend fun fetchImage(request: ImageRequest): Bitmap? =
        fetch(request.source)
            .flatMap { bufferedSource ->
                coroutineContext.ensureActive()
                decode(DecodeParams(bufferedSource, request.imageView.requireSize()))
            }
            .map { bitmap ->
                coroutineContext.ensureActive()
                transform(bitmap, request.transformations)
            }
            .doOnSuccess { bitmap ->
                putCache(request.cacheKey, bitmap)
            }
            .doOnError(Timber::d)
            .data
}
