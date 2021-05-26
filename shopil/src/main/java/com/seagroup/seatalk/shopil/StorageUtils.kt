package com.seagroup.seatalk.shopil

import android.content.Context
import android.os.StatFs
import okhttp3.Cache
import java.io.File

object StorageUtils {
    private const val CACHE_DIRECTORY_NAME = "image_cache"
    private const val MIN_DISK_CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
    private const val MAX_DISK_CACHE_SIZE_BYTES = 250L * 1024 * 1024 // 250MB
    private const val DISK_CACHE_PERCENTAGE = 0.02

    fun createDefaultCache(context: Context): Cache {
        val cacheDirectory = getDefaultCacheDirectory(context)
        val cacheSize = calculateDiskCacheSize(cacheDirectory)
        return Cache(cacheDirectory, cacheSize)
    }

    private fun getDefaultCacheDirectory(context: Context): File =
        File(context.cacheDir, CACHE_DIRECTORY_NAME).apply { mkdirs() }

    private fun calculateDiskCacheSize(cacheDirectory: File): Long =
        try {
            val cacheDir = StatFs(cacheDirectory.absolutePath)
            val size = DISK_CACHE_PERCENTAGE * cacheDir.blockCountLong * cacheDir.blockSizeLong
            size.toLong().coerceIn(MIN_DISK_CACHE_SIZE_BYTES, MAX_DISK_CACHE_SIZE_BYTES)
        } catch (_: Exception) {
            MIN_DISK_CACHE_SIZE_BYTES
        }
}
