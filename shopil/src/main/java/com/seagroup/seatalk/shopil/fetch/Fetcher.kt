package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.request.ImageSource

interface Fetcher<T : ImageSource> {
    suspend fun fetch(source: T): FetchResult
}
