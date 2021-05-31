package com.seagroup.seatalk.shopil.fetcher

import com.seagroup.seatalk.shopil.request.ImageSource
import okhttp3.Call

class DataFetcherFactory(
    private val callFactory: Call.Factory
) {
    fun get(imageSource: ImageSource): Fetcher = when (imageSource) {
        is ImageSource.Drawable -> DrawableFetcher()
        is ImageSource.File -> FileFetcher()
        is ImageSource.Uri -> UriFetcher()
        is ImageSource.Url -> UrlFetcher(callFactory)
    }
}
