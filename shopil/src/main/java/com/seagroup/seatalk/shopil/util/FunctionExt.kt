package com.seagroup.seatalk.shopil.util

internal inline fun <T> invokeWhen(predicate: Boolean, func: () -> T?): T? =
    if (predicate) func() else null
