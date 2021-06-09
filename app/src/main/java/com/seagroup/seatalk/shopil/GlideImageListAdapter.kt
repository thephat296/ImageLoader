package com.seagroup.seatalk.shopil

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class GlideImageListAdapter(
    private val context: Context
) : ImageListAdapter(context) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(getItem(position))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .transform(RoundedCorners(20))
            .into(holder.binding.root)
    }

    override fun toString() = "Glide"
}
