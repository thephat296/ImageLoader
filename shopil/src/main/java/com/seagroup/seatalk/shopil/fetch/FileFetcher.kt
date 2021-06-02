package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource
import okio.buffer
import okio.source

internal class FileFetcher : BaseFetcher<ImageSource.File>() {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun execute(source: ImageSource.File): FetchData =
        FetchData.Source(source = source.data.source().buffer())
}
