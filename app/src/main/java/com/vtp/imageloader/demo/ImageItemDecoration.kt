package com.vtp.imageloader.demo

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ImageItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val space: Int = context.resources.getDimensionPixelSize(R.dimen.margin_tiny)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(space, space, space, space)
    }
}
