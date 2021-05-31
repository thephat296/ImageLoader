package com.seagroup.seatalk.shopil

import android.content.Context
import com.seagroup.seatalk.shopil.fetcher.ContentUriFetcher
import com.seagroup.seatalk.shopil.fetcher.DataFetcherFactory
import com.seagroup.seatalk.shopil.fetcher.DrawableFetcher
import com.seagroup.seatalk.shopil.fetcher.FileFetcher
import com.seagroup.seatalk.shopil.fetcher.FileUriFetcher
import com.seagroup.seatalk.shopil.fetcher.HttpFetcher
import com.seagroup.seatalk.shopil.fetcher.HttpUriFetcher
import com.seagroup.seatalk.shopil.fetcher.HttpUrlFetcher
import com.seagroup.seatalk.shopil.key.CacheKeyFactory
import com.seagroup.seatalk.shopil.memory.DefaultMemoryCache
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.util.MemoryUtils
import com.seagroup.seatalk.shopil.util.StorageUtils
import okhttp3.Call
import okhttp3.OkHttpClient

interface ImageLoader {
    fun enqueue(request: ImageRequest)

    class Builder(context: Context) {
        private val appContext: Context = context.applicationContext

        fun build(): ImageLoader = ImageLoaderImpl(
            appContext = appContext,
            memoryCache = DefaultMemoryCache(MemoryUtils.calculateAvailableMemorySize(appContext)),
            cacheKeyFactory = CacheKeyFactory(),
            dataFetcherFactory = createDataFetcherFactory()
        )

        private fun createDataFetcherFactory(): DataFetcherFactory {
            val callFactory = lazyCallFactory {
                OkHttpClient.Builder()
                    .cache(StorageUtils.createDefaultCache(appContext))
                    .build()
            }
            val httpFetcher = HttpFetcher(callFactory)
            return DataFetcherFactory(
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
