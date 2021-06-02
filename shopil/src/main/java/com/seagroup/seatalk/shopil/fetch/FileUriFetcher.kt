package com.seagroup.seatalk.shopil.fetch

import androidx.core.net.toFile
import com.seagroup.seatalk.shopil.request.ImageSource
import okio.buffer
import okio.source

class FileUriFetcher : BaseFetcher<ImageSource.Uri>() {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun execute(source: ImageSource.Uri): FetchData =
        FetchData.Source(source = source.data.toFile().source().buffer())
}
