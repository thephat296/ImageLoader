package com.vtplib.imageloader.util

internal inline fun <T> invokeWhen(predicate: Boolean, func: () -> T?): T? =
    if (predicate) func() else null
