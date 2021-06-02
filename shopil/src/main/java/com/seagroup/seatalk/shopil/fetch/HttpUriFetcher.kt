package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource
import okhttp3.HttpUrl.Companion.toHttpUrl

class HttpUriFetcher(private val httpFetcher: HttpFetcher) : Fetcher<ImageSource.Uri> {
    override suspend fun fetch(source: ImageSource.Uri): FetchResult =
        httpFetcher.fetch(url = source.data.toString().toHttpUrl())
}
