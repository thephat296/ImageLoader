package com.seagroup.seatalk.shopil

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Size
import androidx.core.graphics.drawable.toDrawable
import com.seagroup.seatalk.shopil.decode.DecodeParams
import com.seagroup.seatalk.shopil.decode.StreamBitmapDecoder
import com.seagroup.seatalk.shopil.fetch.DataFetcherFactory
import com.seagroup.seatalk.shopil.fetch.FetchData
import com.seagroup.seatalk.shopil.fetch.Fetcher
import com.seagroup.seatalk.shopil.key.CacheKey
import com.seagroup.seatalk.shopil.key.CacheKeyFactory
import com.seagroup.seatalk.shopil.memory.MemoryCache
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.request.ImageSource
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

class ImageLoaderImpl(
    private val appContext: Context,
    private val memoryCache: MemoryCache,
    private val cacheKeyFactory: CacheKeyFactory,
    private val dataFetcherFactory: DataFetcherFactory
) : ImageLoader {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate +
            CoroutineExceptionHandler { _, throwable -> Timber.d(throwable) }
    )

    override fun enqueue(request: ImageRequest) {
        fun setImage(drawable: Drawable?) = request.imageView.setImageDrawable(drawable)
        scope.launch {
            request.placeholder?.let {
                setImage(it.getDrawable(appContext))
            }
            val targetSize = request.imageView.requireSize()
            val cacheKey: CacheKey? = cacheKeyFactory.buildKey(request.source, targetSize)
            val cachedValue = cacheKey?.let(memoryCache::get)
            if (cachedValue != null) {
                Timber.d("get value from memory cache")
                return@launch setImage(cachedValue.toDrawable(appContext.resources))
            }

            val fetcher: Fetcher<ImageSource> = dataFetcherFactory.get(request.source) ?: run {
                Timber.d("Unsupported ImageSource[${request.source}]!")
                return@launch setImage(null)
            }

            // Fetch, decode, transform, and cache the image on a background dispatcher.
            val result = withContext(Dispatchers.IO) {
                execute(request, fetcher, targetSize)
            }
            val drawable = when (result) {
                is Result.Success -> {
                    cacheToMemory(cacheKey, result.data)
                    result.data
                }
                is Result.Error -> {
                    Timber.e(result.throwable)
                    request.error?.getDrawable(appContext)
                }
            }
            setImage(drawable)
        }
    }

    private suspend fun execute(
        request: ImageRequest,
        fetcher: Fetcher<ImageSource>,
        targetSize: Size
    ): Result<Drawable> =
        fetcher.fetch(request.source)
            .flatMap {
                coroutineContext.ensureActive()
                processFetchData(it, targetSize)
            }

    private suspend fun processFetchData(data: FetchData, targetSize: Size): Result<Drawable> = when (data) {
        is FetchData.Drawable -> Result.Success(data.drawable)
        is FetchData.Source -> StreamBitmapDecoder(appContext).decode(DecodeParams(data.source, targetSize))
    }

    private fun cacheToMemory(cacheKey: CacheKey?, drawable: Drawable) {
        if (cacheKey == null || drawable !is BitmapDrawable) return
        memoryCache[cacheKey] = drawable.bitmap
    }
}
