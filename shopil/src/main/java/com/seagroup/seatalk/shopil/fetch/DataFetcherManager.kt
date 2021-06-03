package com.seagroup.seatalk.shopil.fetch

import android.content.ContentResolver.SCHEME_CONTENT
import android.content.ContentResolver.SCHEME_FILE
import android.net.Uri
import com.seagroup.seatalk.shopil.Result
import com.seagroup.seatalk.shopil.request.ImageSource

@Suppress("UNCHECKED_CAST")
internal class DataFetcherManager(
    private val drawableFetcher: DrawableFetcher,
    private val contentUriFetcher: ContentUriFetcher,
    private val fileFetcher: FileFetcher,
    private val httpUriFetcher: HttpUriFetcher,
    private val httpUrlFetcher: HttpUrlFetcher,
    private val fileUriFetcher: FileUriFetcher
) {

    suspend fun fetch(imageSource: ImageSource): Result<FetchData> {
        val fetcher = imageSource.getFetcher()
            ?: return Result.Error(IllegalArgumentException("Unsupported ImageSource[$imageSource]"))
        return fetcher.fetch(imageSource)
    }

    private fun ImageSource.getFetcher(): Fetcher<ImageSource>? = when (this) {
        is ImageSource.Drawable -> drawableFetcher
        is ImageSource.File -> fileFetcher
        is ImageSource.Uri -> data.getUriFetcher()
        is ImageSource.Url -> httpUrlFetcher
    } as? Fetcher<ImageSource>

    private fun Uri.getUriFetcher(): Fetcher<ImageSource>? = when (scheme) {
        SCHEME_CONTENT -> contentUriFetcher
        SCHEME_FILE -> fileUriFetcher
        URI_SCHEME_HTTP, URI_SCHEME_HTTPS -> httpUriFetcher
        else -> null
    } as? Fetcher<ImageSource>

    companion object {
        private const val URI_SCHEME_HTTP = "http"
        private const val URI_SCHEME_HTTPS = "https"
    }
}
