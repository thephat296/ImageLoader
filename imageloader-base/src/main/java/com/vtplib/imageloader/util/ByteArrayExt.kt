package com.vtplib.imageloader.util

internal fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
