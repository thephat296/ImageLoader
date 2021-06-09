package com.seagroup.seatalk.shopil

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class HomeViewModel : ViewModel() {
    private val httpClient by lazy(::OkHttpClient)

    fun loadImages(): LiveData<List<String>> = liveData(viewModelScope.coroutineContext) {
        withContext(Dispatchers.IO) {
            val rawData = httpClient
                .newCall(request = Request.Builder().url(URL_IMAGES).build())
                .execute()
                .use { response ->
                    response.body?.string()
                } ?: return@withContext
            val urls = JSONArray(rawData).toList(KEY_DOWNLOAD_URL)
            emit(urls)
        }
    }

    companion object {
        private const val URL_IMAGES = "https://picsum.photos/v2/list?page=2&limit=50"
        private const val KEY_DOWNLOAD_URL = "download_url"
    }
}
