package com.vtplib.imageloader.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.vtplib.imageloader.util.toMD5
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.io.File
import java.util.concurrent.ConcurrentHashMap

internal class DefaultDiskCache(private val cacheDir: File) : DiskCache {
    private val locks = ConcurrentHashMap<CacheKey, Mutex>()
    private val safeCacheDir: File
        get() = cacheDir.apply { mkdirs() }

    private suspend inline fun <T> withLock(key: CacheKey, action: () -> T): T =
        locks.getOrPut(key, ::Mutex)
            .withLock(action = action)

    override suspend fun put(key: CacheKey, bitmap: Bitmap) {
        withLock(key) {
            val file = File(safeCacheDir, key.toFileName())
            if (file.exists()) file.delete()
            try {
                file.outputStream().use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    it.flush()
                }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    override suspend fun get(key: CacheKey): Bitmap? =
        withLock(key) {
            val file = File(safeCacheDir, key.toFileName()).takeIf(File::exists) ?: return@withLock null
            BitmapFactory.decodeFile(file.path)
        }

    override suspend fun delete(key: CacheKey): Boolean =
        withLock(key) {
            File(safeCacheDir, key.toFileName()).delete()
        }

    override suspend fun clear() {
        cacheDir.deleteRecursively()
    }

    private fun CacheKey.toFileName() = toString().toMD5()
}
