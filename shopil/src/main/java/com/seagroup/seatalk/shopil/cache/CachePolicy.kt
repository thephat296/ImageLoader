package com.seagroup.seatalk.shopil.cache

enum class CachePolicy {
    DISABLED,
    ENABLED;

    val enabled: Boolean
        get() = this == ENABLED
}
