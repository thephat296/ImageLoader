package com.seagroup.seatalk.shopil.fetch

import android.accounts.NetworkErrorException
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request
import okio.BufferedSource

internal class HttpFetcher(private val callFactory: Call.Factory) {
    @Suppress("BlockingMethodInNonBlockingContext")
    fun fetch(url: HttpUrl): BufferedSource {
        val request = Request.Builder().url(url)
        val response = callFactory.newCall(request.build()).execute()
        val body = response.body
        if (!response.isSuccessful || body == null) {
            response.body?.close()
            throw NetworkErrorException()
        }
        return body.source()
    }
}
