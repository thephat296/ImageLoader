package com.seagroup.seatalk.shopil.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.seagroup.seatalk.shopil.util.toMD5
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.io.File

internal class DefaultDiskCache(private val cacheDir: File) : DiskCache {
    private val locks = HashMap<CacheKey, Mutex>()
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
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

    @Synchronized
    override suspend fun clear() {
        cacheDir.deleteRecursively()
    }

    private fun CacheKey.toFileName() = toString().toMD5()
}
