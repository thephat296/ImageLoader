package com.seagroup.seatalk.shopil.fetch

import android.content.Context
import com.seagroup.seatalk.shopil.request.ImageSource
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.InputStream

internal class ContentUriFetcher(private val context: Context) : BaseFetcher<ImageSource.Uri>() {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun execute(source: ImageSource.Uri): BufferedSource {
        val inputStream: InputStream = context.contentResolver.openInputStream(source.data)
            ?: throw IllegalStateException("Unable to open ${source.data}")
        return inputStream.source().buffer()
    }
}
