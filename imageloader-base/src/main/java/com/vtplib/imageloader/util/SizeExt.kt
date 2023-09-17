package com.vtplib.imageloader.util

import android.util.Size

internal fun Size.scaleDownTo(time: Int) = Size(width / time, height / time)
