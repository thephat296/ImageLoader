package com.seagroup.seatalk.shopil.fetcher

import com.seagroup.seatalk.shopil.request.ImageSource

class DrawableFetcher : Fetcher<ImageSource.Drawable> {
    override suspend fun fetch(source: ImageSource.Drawable) = FetchResult.Drawable(source.data)
}
