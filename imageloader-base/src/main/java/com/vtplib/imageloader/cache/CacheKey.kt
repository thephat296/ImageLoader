package com.vtplib.imageloader.cache

import android.os.Parcelable
import android.util.Size
import kotlinx.parcelize.Parcelize

@Parcelize
data class CacheKey(
    val source: String,
    val targetSize: Size,
    val transformations: List<String>?
) : Parcelable
