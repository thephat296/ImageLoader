package com.seagroup.seatalk.shopil.request

import android.view.View
import kotlinx.coroutines.Job

class ImageJobManager : View.OnAttachStateChangeListener {

    private var job: Job? = null

    fun setCurrentJob(job: Job) {
        this.job = job
    }

    fun cancelCurrentJob() {
        if (job?.isActive == true) {
            job?.cancel()
            job = null
        }
    }

    override fun onViewAttachedToWindow(v: View?) = Unit

    override fun onViewDetachedFromWindow(v: View?) = cancelCurrentJob()
}
