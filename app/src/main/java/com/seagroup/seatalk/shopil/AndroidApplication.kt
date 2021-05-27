package com.seagroup.seatalk.shopil

import android.app.Application
import timber.log.Timber

class AndroidApplication : Application() {
    lateinit var imageLoader: ImageLoader
        private set

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        imageLoader = ImageLoader.Builder(this).build()
    }
}
