package com.seagroup.seatalk.shopil

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.internal.closeQuietly
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class ImageLoaderImpl(
    private val appContext: Context,
    private val memoryCache: MemoryCache,
    private val callFactory: Call.Factory,
    private val cacheKeyFactory: CacheKeyFactory,
    private val dataFetcherFactory: DataFetcherFactory
) : ImageLoader {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate +
            CoroutineExceptionHandler { _, throwable -> Timber.d(throwable) }
    )

    override fun enqueue(request: ImageRequest) {
        scope.launch {
            request.placeholder?.let {
                request.imageView.setImageDrawable(it.getDrawable(appContext))
            }
            val cacheKey: CacheKey? = cacheKeyFactory.buildKey(request.source)
            val cachedValue = cacheKey?.let(memoryCache::get)
            if (cachedValue != null) {
                Timber.d("get value from memory cache")
                request.imageView.setImageDrawable(
                    cachedValue.toDrawable(appContext.resources)
                )
                return@launch
            }

            // Fetch, decode, transform, and cache the image on a background dispatcher.
            val fetcher = dataFetcherFactory.get(request.source)
            val result = withContext(Dispatchers.IO) {
                execute(request, fetcher)
                    .doOnSuccess {
                        val bitmap = (it as? BitmapDrawable)?.bitmap ?: return@doOnSuccess
                        if (cacheKey != null) {
                            memoryCache[cacheKey] = bitmap
                        }
                    }
            }
            val drawable: Drawable? = when (result) {
                is ImageResult.Success -> result.drawable
                is ImageResult.Error -> request.error?.getDrawable(appContext)
            }
            request.imageView.setImageDrawable(drawable)
        }
    }

    private suspend fun execute(request: ImageRequest, fetcher: Fetcher): ImageResult {
        return when (val fetchResult = fetcher.fetch(request.source)) {
            is FetchResult.Source -> try {
                coroutineContext.ensureActive()
                val drawable = StreamBitmapDecoder(appContext).decode(fetchResult.source)
                ImageResult.Success(drawable)
            } catch (throwable: Throwable) {
                fetchResult.source.closeQuietly()
                ImageResult.Error(throwable)
            }
            is FetchResult.Drawable -> ImageResult.Success(fetchResult.drawable)
        }
    }
}
