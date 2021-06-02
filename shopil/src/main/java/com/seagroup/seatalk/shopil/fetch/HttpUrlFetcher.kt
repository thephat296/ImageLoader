package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource
import okhttp3.HttpUrl.Companion.toHttpUrl

class HttpUrlFetcher(private val httpFetcher: HttpFetcher) : Fetcher<ImageSource.Url> {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(source: ImageSource.Url): FetchResult =
        httpFetcher.fetch(url = source.data.toHttpUrl())
}
