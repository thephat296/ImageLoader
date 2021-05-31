package com.seagroup.seatalk.shopil.fetcher

import android.accounts.NetworkErrorException
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request
import timber.log.Timber

class HttpFetcher(private val callFactory: Call.Factory) {
    @Suppress("BlockingMethodInNonBlockingContext")
    fun fetch(url: HttpUrl): FetchResult {
        val request = Request.Builder().url(url)
        val response = callFactory.newCall(request.build()).execute()
        val body = response.body
        if (!response.isSuccessful || body == null) {
            response.body?.close()
            throw NetworkErrorException()
        }
        val useDiskCache = response.cacheResponse != null
        Timber.d("is using disk cache: $useDiskCache")
        return FetchResult.Source(body.source())
    }
}
