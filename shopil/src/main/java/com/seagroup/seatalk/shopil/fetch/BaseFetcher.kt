package com.seagroup.seatalk.shopil.fetch

import com.seagroup.seatalk.shopil.Result
import com.seagroup.seatalk.shopil.request.ImageSource

abstract class BaseFetcher<T : ImageSource> : Fetcher<T> {
    protected abstract suspend fun execute(source: T): FetchData

    override suspend fun fetch(source: T): Result<FetchData> =
        try {
            Result.Success(execute(source))
        } catch (e: Throwable) {
            Result.Error(e)
        }
}
