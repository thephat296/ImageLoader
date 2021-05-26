package com.seagroup.seatalk.shopil

import android.content.Context
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.internal.closeQuietly
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class ImageLoaderImpl(
    private val appContext: Context
) : ImageLoader {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate +
            CoroutineExceptionHandler { _, throwable -> Timber.d(throwable) }
    )

    override fun enqueue(request: ImageRequest) {
        scope.launch {
            // TODO: Check the memory cache.

            // Fetch, decode, transform, and cache the image on a background dispatcher.
            val drawable = withContext(Dispatchers.IO) {
                execute(request)
            }
            request.imageView.setImageDrawable(drawable)
        }
    }

    private suspend fun execute(request: ImageRequest): Drawable {
        val callFactory = OkHttpClient.Builder()
            .cache(StorageUtils.createDefaultCache(appContext))
            .build()
        val fetcher = HttpUrlFetcher(callFactory)

        return when (val fetchResult = fetcher.fetch(request.imgUrl.toHttpUrl())) {
            is FetchResult.Source -> try {
                coroutineContext.ensureActive()
                BitmapDecoder(appContext).decode(fetchResult.source)
            } catch (throwable: Throwable) {
                fetchResult.source.closeQuietly()
                throw throwable
            }
            is FetchResult.Drawable -> fetchResult.drawable
        }
    }
}
