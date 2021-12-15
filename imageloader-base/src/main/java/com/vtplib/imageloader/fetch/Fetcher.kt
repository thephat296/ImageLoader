package com.vtplib.imageloader.fetch

import com.vtplib.imageloader.Result
import com.vtplib.imageloader.request.ImageSource
import okio.BufferedSource

internal interface Fetcher<T : ImageSource> {
    suspend fun fetch(source: T): Result<BufferedSource>
}
