package com.seagroup.seatalk.shopil

interface Fetcher<T> {
    suspend fun fetch(data: T): FetchResult
}
