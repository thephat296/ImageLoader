package com.seagroup.seatalk.shopil

interface Fetcher<T> {

    /**
     * Compute the memory cache key for [data].
     */
    fun key(data: T): String

    suspend fun fetch(data: T): FetchResult
}
