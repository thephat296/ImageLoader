package com.seagroup.seatalk.shopil.key

import android.os.Parcelable
import android.util.Size
import kotlinx.parcelize.Parcelize

@Parcelize
data class CacheKey(val source: String, val size: Size) : Parcelable
