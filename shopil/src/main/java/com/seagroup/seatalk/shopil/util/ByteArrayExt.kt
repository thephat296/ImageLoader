package com.seagroup.seatalk.shopil.util

internal fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
