package com.vtplib.imageloader

import android.content.Context
import com.vtplib.imageloader.cache.CacheManager
import com.vtplib.imageloader.cache.CacheManagerImpl
import com.vtplib.imageloader.cache.DefaultDiskCache
import com.vtplib.imageloader.cache.DefaultMemoryCache
import com.vtplib.imageloader.decode.StreamBitmapDecoder
import com.vtplib.imageloader.fetch.*
import com.vtplib.imageloader.request.ImageRequest
import com.vtplib.imageloader.request.ImageSource
import com.vtplib.imageloader.transform.TransformerImpl
import com.vtplib.imageloader.util.MemoryUtils
import com.vtplib.imageloader.util.StorageUtils
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

interface ImageLoader : CacheManager {
    fun enqueue(request: ImageRequest)

    class Builder(context: Context) {
        private val appContext: Context = context.applicationContext

        fun build(): ImageLoader = ImageLoaderImpl(
            appContext = appContext,
            cacheManager = CacheManagerImpl(
                memoryCache = DefaultMemoryCache(MemoryUtils.calculateAvailableMemorySize(appContext)),
                diskCache = DefaultDiskCache(StorageUtils.getDefaultCacheDirectory(appContext))
            ),
            fetcher = createFetcher(),
            decoder = StreamBitmapDecoder(),
            transformer = TransformerImpl()
        )

        private fun createFetcher(): Fetcher<ImageSource> {
            val callFactory =
                OkHttpClient.Builder()
                    //                    .cache(StorageUtils.createDefaultCache(appContext))
                    .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .build()
            val httpFetcher = HttpFetcher(callFactory)
            return MediatorFetcher(
                contentUriFetcher = ContentUriFetcher(appContext),
                fileFetcher = FileFetcher(),
                httpUriFetcher = HttpUriFetcher(httpFetcher),
                httpUrlFetcher = HttpUrlFetcher(httpFetcher),
                fileUriFetcher = FileUriFetcher()
            )
        }
    }

    companion object {
        private const val CONNECTION_TIME_OUT = 30L
        private const val READ_TIME_OUT = 30L
    }
}
