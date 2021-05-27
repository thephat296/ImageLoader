package com.seagroup.seatalk.shopil

import android.content.Context
import okhttp3.Call
import okhttp3.OkHttpClient

interface ImageLoader {
    fun enqueue(request: ImageRequest)

    class Builder(context: Context) {
        private val appContext: Context = context.applicationContext
        private var memoryCache: MemoryCache =
            DefaultMemoryCache(MemoryUtils.calculateAvailableMemorySize(context.applicationContext))
        private var callFactory: Call.Factory

        init {
            callFactory = lazyCallFactory {
                OkHttpClient.Builder()
                    .cache(StorageUtils.createDefaultCache(appContext))
                    .build()
            }
        }

        private fun lazyCallFactory(initializer: () -> Call.Factory): Call.Factory {
            val lazy: Lazy<Call.Factory> = lazy(initializer)
            return Call.Factory(lazy.value::newCall)
        }

        fun build(): ImageLoader = ImageLoaderImpl(appContext, memoryCache, callFactory)
    }
}
