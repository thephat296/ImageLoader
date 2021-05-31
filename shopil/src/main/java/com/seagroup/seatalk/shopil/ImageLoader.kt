package com.seagroup.seatalk.shopil

import android.content.Context
import com.seagroup.seatalk.shopil.fetcher.DataFetcherFactory
import com.seagroup.seatalk.shopil.key.CacheKeyFactory
import com.seagroup.seatalk.shopil.memory.DefaultMemoryCache
import com.seagroup.seatalk.shopil.memory.MemoryCache
import com.seagroup.seatalk.shopil.request.ImageRequest
import com.seagroup.seatalk.shopil.util.MemoryUtils
import com.seagroup.seatalk.shopil.util.StorageUtils
import okhttp3.Call
import okhttp3.OkHttpClient

interface ImageLoader {
    fun enqueue(request: ImageRequest)

    class Builder(context: Context) {
        private val appContext: Context = context.applicationContext
        private var memoryCache: MemoryCache =
            DefaultMemoryCache(MemoryUtils.calculateAvailableMemorySize(context.applicationContext))
        private var callFactory: Call.Factory
        private var cacheKeyFactory: CacheKeyFactory
        private var dataFetcherFactory: DataFetcherFactory

        init {
            callFactory = lazyCallFactory {
                OkHttpClient.Builder()
                    .cache(StorageUtils.createDefaultCache(appContext))
                    .build()
            }
            cacheKeyFactory = CacheKeyFactory()
            dataFetcherFactory = DataFetcherFactory(callFactory)
        }

        private fun lazyCallFactory(initializer: () -> Call.Factory): Call.Factory {
            val lazy: Lazy<Call.Factory> = lazy(initializer)
            return Call.Factory(lazy.value::newCall)
        }

        fun build(): ImageLoader = ImageLoaderImpl(
            appContext,
            memoryCache,
            callFactory,
            cacheKeyFactory,
            dataFetcherFactory
        )
    }
}
