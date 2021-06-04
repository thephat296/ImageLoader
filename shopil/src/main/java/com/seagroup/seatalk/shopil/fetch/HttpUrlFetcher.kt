package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource
import okhttp3.HttpUrl.Companion.toHttpUrl
import okio.BufferedSource

internal class HttpUrlFetcher(private val httpFetcher: HttpFetcher) : BaseFetcher<ImageSource.Url>() {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun execute(source: ImageSource.Url): BufferedSource =
        httpFetcher.fetch(url = source.data.toHttpUrl())
}
