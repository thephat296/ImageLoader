package com.vtplib.imageloader.cache

enum class CachePolicy {
    DISABLED,
    ENABLED;

    val enabled: Boolean
        get() = this == ENABLED
}
