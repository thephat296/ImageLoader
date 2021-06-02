package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource
import okio.buffer
import okio.source

class FileFetcher : Fetcher<ImageSource.File> {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(source: ImageSource.File): FetchResult =
        FetchResult.Source(source = source.data.source().buffer())
}
