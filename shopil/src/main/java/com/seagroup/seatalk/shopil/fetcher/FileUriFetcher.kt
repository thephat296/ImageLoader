package com.seagroup.seatalk.shopil.fetcher

import androidx.core.net.toFile
import com.seagroup.seatalk.shopil.request.ImageSource
import okio.buffer
import okio.source

class FileUriFetcher : Fetcher<ImageSource.Uri> {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(source: ImageSource.Uri): FetchResult =
        FetchResult.Source(source = source.data.toFile().source().buffer())
}
