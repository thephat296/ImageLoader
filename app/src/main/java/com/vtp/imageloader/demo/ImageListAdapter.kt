package com.vtp.imageloader.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vtp.imageloader.demo.databinding.ItemImageBinding
import com.vtplib.imageloader.cache.CachePolicy
import com.vtplib.imageloader.request.ImageResource
import com.vtplib.imageloader.transform.RoundedCornerTransformation
import com.vtplib.imageloader.util.load

typealias Url = String

open class ImageListAdapter(
    private val context: Context
) : ListAdapter<Url, ImageListAdapter.ViewHolder>(Callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.load(url = getItem(position)) {
            placeholder(ImageResource.DrawableRes(R.drawable.ic_launcher_foreground))
            error(ImageResource.DrawableRes(R.drawable.ic_launcher_background))
            transformations(RoundedCornerTransformation())
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
        }
    }

    class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    private object Callback : DiffUtil.ItemCallback<Url>() {
        override fun areItemsTheSame(old: Url, new: Url) = old == new
        override fun areContentsTheSame(old: Url, new: Url) = old == new
    }

    companion object {
        const val NUM_COLUMNS = 10
    }

    override fun toString() = "VtpImageLoader"
}
