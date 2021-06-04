package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.Result
import com.seagroup.seatalk.shopil.request.ImageSource
import okio.BufferedSource

internal interface Fetcher<T : ImageSource> {
    suspend fun fetch(source: T): Result<BufferedSource>
}
