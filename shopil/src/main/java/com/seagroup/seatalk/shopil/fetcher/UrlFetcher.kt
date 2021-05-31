package com.seagroup.seatalk.shopil.fetcher

import android.accounts.NetworkErrorException
import com.seagroup.seatalk.shopil.request.ImageSource
import okhttp3.Call
import okhttp3.Request
import timber.log.Timber

class UrlFetcher(private val callFactory: Call.Factory) : Fetcher {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(source: ImageSource): FetchResult {
        require(source is ImageSource.Url)
        val request = Request.Builder().url(source.data)
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
