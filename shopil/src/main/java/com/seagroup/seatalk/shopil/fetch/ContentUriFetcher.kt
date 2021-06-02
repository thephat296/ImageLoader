package com.seagroup.seatalk.shopil.fetch

import android.content.Context
import com.seagroup.seatalk.shopil.request.ImageSource
import okio.buffer
import okio.source
import java.io.InputStream

class ContentUriFetcher(private val context: Context) : Fetcher<ImageSource.Uri> {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(source: ImageSource.Uri): FetchResult {
        val inputStream: InputStream? = context.contentResolver.openInputStream(source.data)
        return if (inputStream == null) {
            FetchResult.Error(IllegalStateException("Unable to open ${source.data}"))
        } else {
            FetchResult.Source(inputStream.source().buffer())
        }
    }
}
