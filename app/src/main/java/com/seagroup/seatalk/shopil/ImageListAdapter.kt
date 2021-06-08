package com.seagroup.seatalk.shopil

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seagroup.seatalk.shopil.databinding.ItemImageBinding
import com.seagroup.seatalk.shopil.request.ImageResource
import com.seagroup.seatalk.shopil.transform.BlurTransformation
import com.seagroup.seatalk.shopil.util.load

typealias Url = String

class ImageListAdapter(
    private val context: Context
) : ListAdapter<Url, ImageListAdapter.ViewHolder>(Callback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.load(url = getItem(position)) {
            placeholder(ImageResource.DrawableRes(R.drawable.ic_launcher_background))
            error(ImageResource.DrawableRes(R.drawable.ic_launcher_foreground))
            transformations(BlurTransformation(context))
        }
    }

    class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    private object Callback : DiffUtil.ItemCallback<Url>() {
        override fun areItemsTheSame(old: Url, new: Url) = old == new
        override fun areContentsTheSame(old: Url, new: Url) = old == new
    }

    companion object {
        const val NUM_COLUMNS = 3
    }
}
