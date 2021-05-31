package com.seagroup.seatalk.shopil.fetcher

import com.seagroup.seatalk.shopil.request.ImageSource

interface Fetcher {
    suspend fun fetch(source: ImageSource): FetchResult
}
