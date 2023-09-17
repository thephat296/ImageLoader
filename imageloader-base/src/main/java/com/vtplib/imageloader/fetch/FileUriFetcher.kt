package com.vtplib.imageloader.fetch

import androidx.core.net.toFile
import com.vtplib.imageloader.request.ImageSource
import okio.BufferedSource
import okio.buffer
import okio.source

internal class FileUriFetcher : BaseFetcher<ImageSource.Uri>() {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun execute(source: ImageSource.Uri): BufferedSource = source.data.toFile().source().buffer()
}
