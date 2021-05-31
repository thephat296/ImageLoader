package com.seagroup.seatalk.shopil

interface Fetcher {
    suspend fun fetch(source: ImageSource): FetchResult
}
