package com.seagroup.seatalk.shopil

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Size
import androidx.core.graphics.drawable.toDrawable
import com.seagroup.seatalk.shopil.cache.CacheKey
import com.seagroup.seatalk.shopil.cache.MemoryCache
import com.seagroup.seatalk.shopil.decode.DecodeParams
import com.seagroup.seatalk.shopil.decode.StreamBitmapDecoder
import com.seagroup.seatalk.shopil.fetch.DataFetcherManager
import com.seagroup.seatalk.shopil.fetch.FetchData
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.transform.Transformation
import com.seagroup.seatalk.shopil.util.awaitSize
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
    private val memoryCache: MemoryCache,
    private val dataFetcherManager: DataFetcherManager
) : ImageLoader {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate +
            CoroutineExceptionHandler { _, throwable -> Timber.d(throwable) }
    )

    override fun enqueue(request: ImageRequest) {
        fun setImage(drawable: Drawable?) = request.imageView.setImageDrawable(drawable)
        scope.launch {
            request.placeholder?.getDrawable(appContext)?.let(::setImage)
            request.imageView.awaitSize()

            withContext(Dispatchers.IO) {
                val cachedImage = request.cacheKey?.let(memoryCache::get)?.toDrawable(appContext.resources)
                cachedImage ?: fetchImage(request)
            }.let(::setImage)
        }
    }

    private suspend fun fetchImage(request: ImageRequest): Drawable? =
        dataFetcherManager.fetch(request.source)
            .flatMap { drawable ->
                coroutineContext.ensureActive()
                decode(drawable, request.imageView.requireSize())
            }
            .map { drawable ->
                coroutineContext.ensureActive()
                applyTransformations(drawable, request.transformations)
            }
            .doOnSuccess {
                cacheToMemory(request.cacheKey, it)
            }
            .doOnError(Timber::d)
            .data ?: request.error?.getDrawable(appContext)

    private suspend fun decode(data: FetchData, targetSize: Size): Result<Drawable> = when (data) {
        is FetchData.Drawable -> Result.Success(data.drawable)
        is FetchData.Source -> StreamBitmapDecoder(appContext)
            .decode(DecodeParams(data.source, targetSize))
    }

    private suspend fun applyTransformations(drawable: Drawable, transformations: List<Transformation>?): Drawable {
        if (drawable !is BitmapDrawable || transformations.isNullOrEmpty()) return drawable
        return transformations.fold(drawable.bitmap) { bitmap, transformation ->
            transformation.transform(bitmap)
        }
            .toDrawable(appContext.resources)
    }

    private fun cacheToMemory(cacheKey: CacheKey?, drawable: Drawable) {
        if (cacheKey == null || drawable !is BitmapDrawable) return
        memoryCache[cacheKey] = drawable.bitmap
    }
}
