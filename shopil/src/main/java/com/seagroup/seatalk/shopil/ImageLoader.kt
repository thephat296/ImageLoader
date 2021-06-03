package com.seagroup.seatalk.shopil

import android.content.Context
import com.seagroup.seatalk.shopil.cache.CacheManagerImpl
import com.seagroup.seatalk.shopil.cache.DefaultDiskCache
import com.seagroup.seatalk.shopil.cache.DefaultMemoryCache
import com.seagroup.seatalk.shopil.fetch.ContentUriFetcher
import com.seagroup.seatalk.shopil.fetch.DataFetcherManager
import com.seagroup.seatalk.shopil.fetch.DrawableFetcher
import com.seagroup.seatalk.shopil.fetch.FileFetcher
import com.seagroup.seatalk.shopil.fetch.FileUriFetcher
import com.seagroup.seatalk.shopil.fetch.HttpFetcher
import com.seagroup.seatalk.shopil.fetch.HttpUriFetcher
import com.seagroup.seatalk.shopil.fetch.HttpUrlFetcher
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.util.MemoryUtils
import com.seagroup.seatalk.shopil.util.StorageUtils
import okhttp3.Call
import okhttp3.OkHttpClient

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
            dataFetcherManager = createDataFetcherManager()
        )

        private fun createDataFetcherManager(): DataFetcherManager {
            val callFactory = lazyCallFactory {
                OkHttpClient.Builder()
//                    .cache(StorageUtils.createDefaultCache(appContext))
                    .build()
            }
            val httpFetcher = HttpFetcher(callFactory)
            return DataFetcherManager(
                drawableFetcher = DrawableFetcher(),
                contentUriFetcher = ContentUriFetcher(appContext),
                fileFetcher = FileFetcher(),
                httpUriFetcher = HttpUriFetcher(httpFetcher),
                httpUrlFetcher = HttpUrlFetcher(httpFetcher),
                fileUriFetcher = FileUriFetcher()
            )
        }

        private fun lazyCallFactory(initializer: () -> Call.Factory): Call.Factory {
            val lazy: Lazy<Call.Factory> = lazy(initializer)
            return Call.Factory(lazy.value::newCall)
        }
    }
}
