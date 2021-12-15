package com.vtplib.imageloader.fetch

import com.vtplib.imageloader.request.ImageSource
import okio.BufferedSource
import okio.buffer
import okio.source

internal class FileFetcher : BaseFetcher<ImageSource.File>() {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun execute(source: ImageSource.File): BufferedSource = source.data.source().buffer()
}
