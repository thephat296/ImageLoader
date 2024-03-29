package com.vtplib.imageloader.fetch

import com.vtplib.imageloader.request.ImageSource
import okhttp3.HttpUrl.Companion.toHttpUrl
import okio.BufferedSource

internal class HttpUriFetcher(private val httpFetcher: HttpFetcher) : BaseFetcher<ImageSource.Uri>() {
    override suspend fun execute(source: ImageSource.Uri): BufferedSource =
        httpFetcher.fetch(url = source.data.toString().toHttpUrl())
}
