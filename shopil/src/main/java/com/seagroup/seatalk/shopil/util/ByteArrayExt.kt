package com.seagroup.seatalk.shopil.util

fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
