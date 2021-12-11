package com.vtplib.imageloader.fetch

import com.vtplib.imageloader.Result
import com.vtplib.imageloader.request.ImageSource
import okio.BufferedSource

internal abstract class BaseFetcher<T : ImageSource> : Fetcher<T> {
    protected abstract suspend fun execute(source: T): BufferedSource

    override suspend fun fetch(source: T): Result<BufferedSource> =
        try {
            Result.Success(execute(source))
        } catch (e: Throwable) {
            Result.Error(e)
        }
}
