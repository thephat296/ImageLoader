package com.vtplib.imageloader.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.getSystemService

internal object MemoryUtils {
    private const val DEFAULT_MEMORY_CLASS_MEGABYTES = 256
    private const val DEFAULT_AVAILABLE_MEMORY_PERCENTAGE = 0.25

    fun calculateAvailableMemorySize(
        context: Context,
        percentage: Double = DEFAULT_AVAILABLE_MEMORY_PERCENTAGE
    ): Int {
        val memoryClassMegabytes = try {
            val activityManager: ActivityManager = context.getSystemService()!!
            val isLargeHeap = (context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0
            if (isLargeHeap) activityManager.largeMemoryClass else activityManager.memoryClass
        } catch (_: Exception) {
            DEFAULT_MEMORY_CLASS_MEGABYTES
        }
        return (percentage * memoryClassMegabytes * 1024 * 1024).toInt()
    }
}
