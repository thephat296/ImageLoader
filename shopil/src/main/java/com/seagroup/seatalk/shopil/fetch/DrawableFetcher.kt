package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource

class DrawableFetcher : BaseFetcher<ImageSource.Drawable>() {
    override suspend fun execute(source: ImageSource.Drawable) = FetchData.Drawable(source.data)
}
