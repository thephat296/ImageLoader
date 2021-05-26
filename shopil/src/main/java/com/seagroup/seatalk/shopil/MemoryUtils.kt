package com.seagroup.seatalk.shopil

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.getSystemService

object MemoryUtils {
    private const val DEFAULT_MEMORY_CLASS_MEGABYTES = 256

    fun calculateAvailableMemorySize(context: Context, percentage: Double): Int {
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
