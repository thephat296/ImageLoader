package com.seagroup.seatalk.shopil

import android.accounts.NetworkErrorException
import android.net.Uri
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import timber.log.Timber

internal class HttpUriFetcher(callFactory: Call.Factory) : HttpFetcher<Uri>(callFactory) {
    override fun Uri.toHttpUrl(): HttpUrl = toString().toHttpUrl()
}

internal class HttpUrlFetcher(callFactory: Call.Factory) : HttpFetcher<HttpUrl>(callFactory) {
    override fun HttpUrl.toHttpUrl(): HttpUrl = this
}

internal abstract class HttpFetcher<T : Any>(private val callFactory: Call.Factory) : Fetcher<T> {

    abstract fun T.toHttpUrl(): HttpUrl

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(data: T): FetchResult {
        val url = data.toHttpUrl()
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
