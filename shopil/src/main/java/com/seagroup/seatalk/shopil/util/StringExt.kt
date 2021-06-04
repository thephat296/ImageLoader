package com.seagroup.seatalk.shopil.util

import java.security.MessageDigest

private const val ALGORITHM_MD5 = "MD5"

internal fun String.toMD5(): String =
    MessageDigest.getInstance(ALGORITHM_MD5)
        .digest(this.toByteArray())
        .toHex()
