package com.vtplib.imageloader.fetch

import android.accounts.NetworkErrorException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.internal.closeQuietly
import okio.BufferedSource
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class HttpFetcher(private val callFactory: Call.Factory) {
    suspend fun fetch(url: HttpUrl): BufferedSource =
        suspendCancellableCoroutine { continuation ->
            val request = Request.Builder().url(url)
            val response = callFactory.newCall(request.build()).execute()
            val body = response.body
            if (!response.isSuccessful || body == null) {
                body?.close()
                return@suspendCancellableCoroutine continuation.resumeWithException(NetworkErrorException())
            }
            continuation.resume(body.source())
            continuation.invokeOnCancellation {
                body.closeQuietly()
            }
        }
}
