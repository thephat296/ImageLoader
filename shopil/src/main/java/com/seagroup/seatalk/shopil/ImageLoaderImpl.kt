package com.seagroup.seatalk.shopil

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import com.seagroup.seatalk.shopil.decoder.StreamBitmapDecoder
import com.seagroup.seatalk.shopil.fetcher.DataFetcherFactory
import com.seagroup.seatalk.shopil.fetcher.FetchResult
import com.seagroup.seatalk.shopil.fetcher.Fetcher
import com.seagroup.seatalk.shopil.key.CacheKey
import com.seagroup.seatalk.shopil.key.CacheKeyFactory
import com.seagroup.seatalk.shopil.memory.MemoryCache
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.request.ImageResult
import com.seagroup.seatalk.shopil.request.ImageSource
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
            val cacheKey: CacheKey? = cacheKeyFactory.buildKey(request.source)
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
                execute(request, fetcher)
            }
            val drawable = when (result) {
                is ImageResult.Success -> {
                    cacheToMemory(cacheKey, result.drawable)
                    result.drawable
                }
                is ImageResult.Error -> {
                    Timber.e(result.throwable)
                    request.error?.getDrawable(appContext)
                }
            }
            setImage(drawable)
        }
    }

    private suspend fun execute(request: ImageRequest, fetcher: Fetcher<ImageSource>): ImageResult {
        return when (val fetchResult = fetcher.fetch(request.source)) {
            is FetchResult.Source -> {
                coroutineContext.ensureActive()
                StreamBitmapDecoder(appContext).decode(source = fetchResult.source).mapToImageResult()
            }
            is FetchResult.Drawable -> ImageResult.Success(fetchResult.drawable)
            is FetchResult.Error -> ImageResult.Error(fetchResult.exception)
        }
    }

    private fun cacheToMemory(cacheKey: CacheKey?, drawable: Drawable) {
        if (cacheKey == null || drawable !is BitmapDrawable) return
        memoryCache[cacheKey] = drawable.bitmap
    }
}
