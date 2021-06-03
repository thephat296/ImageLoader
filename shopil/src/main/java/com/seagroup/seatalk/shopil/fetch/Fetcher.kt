package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.Result
import com.seagroup.seatalk.shopil.request.ImageSource

internal interface Fetcher<T : ImageSource> {
    suspend fun fetch(source: T): Result<FetchData>
}