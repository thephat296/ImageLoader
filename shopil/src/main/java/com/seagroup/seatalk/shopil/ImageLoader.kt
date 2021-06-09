package com.seagroup.seatalk.shopil

import android.content.Context
import com.seagroup.seatalk.shopil.cache.CacheManager
import com.seagroup.seatalk.shopil.cache.CacheManagerImpl
import com.seagroup.seatalk.shopil.cache.DefaultDiskCache
import com.seagroup.seatalk.shopil.cache.DefaultMemoryCache
import com.seagroup.seatalk.shopil.decode.StreamBitmapDecoder
import com.seagroup.seatalk.shopil.fetch.ContentUriFetcher
import com.seagroup.seatalk.shopil.fetch.Fetcher
import com.seagroup.seatalk.shopil.fetch.FileFetcher
import com.seagroup.seatalk.shopil.fetch.FileUriFetcher
import com.seagroup.seatalk.shopil.fetch.HttpFetcher
import com.seagroup.seatalk.shopil.fetch.HttpUriFetcher
import com.seagroup.seatalk.shopil.fetch.HttpUrlFetcher
import com.seagroup.seatalk.shopil.fetch.MediatorFetcher
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.request.ImageSource
import com.seagroup.seatalk.shopil.transform.TransformerImpl
import com.seagroup.seatalk.shopil.util.MemoryUtils
import com.seagroup.seatalk.shopil.util.StorageUtils
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

    companion object {
        private const val CONNECTION_TIME_OUT = 10L
        private const val READ_TIME_OUT = 10L
    }
}
