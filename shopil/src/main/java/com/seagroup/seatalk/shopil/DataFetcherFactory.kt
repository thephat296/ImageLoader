package com.seagroup.seatalk.shopil

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
