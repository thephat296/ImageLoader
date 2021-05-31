package com.seagroup.seatalk.shopil.fetcher

import com.seagroup.seatalk.shopil.request.ImageSource

interface Fetcher<T : ImageSource> {
    suspend fun fetch(source: T): FetchResult
}
