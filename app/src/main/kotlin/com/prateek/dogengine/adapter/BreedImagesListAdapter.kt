package com.prateek.dogengine.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prateek.dogengine.R
import com.prateek.dogengine.data.BreedImage

class BreedImagesListAdapter : PagedListAdapter<BreedImage, BreedImagesListAdapter.ViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_result, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(getItem(position)!!.mUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.mImage)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImage: ImageView = itemView.findViewById(R.id.image)
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<BreedImage> =
            object : DiffUtil.ItemCallback<BreedImage>() {
                override fun areItemsTheSame(oldItem: BreedImage, newItem: BreedImage): Boolean {
                    return oldItem.mId === newItem.mId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: BreedImage, newItem: BreedImage): Boolean {
                    return oldItem == newItem
                }
            }
    }
}