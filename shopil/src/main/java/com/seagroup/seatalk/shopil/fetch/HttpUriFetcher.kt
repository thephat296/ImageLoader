package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource
import okhttp3.HttpUrl.Companion.toHttpUrl

internal class HttpUriFetcher(private val httpFetcher: HttpFetcher) : BaseFetcher<ImageSource.Uri>() {
    override suspend fun execute(source: ImageSource.Uri): FetchData =
        httpFetcher.fetch(url = source.data.toString().toHttpUrl())
}
