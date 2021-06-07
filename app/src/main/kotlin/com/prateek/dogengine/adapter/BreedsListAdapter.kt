package com.prateek.dogengine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prateek.dogengine.BreedImagesFragment
import com.prateek.dogengine.R
import com.prateek.dogengine.data.Breed
import com.prateek.dogengine.databinding.ItemBreedBinding

class BreedsListAdapter : RecyclerView.Adapter<BreedsListAdapter.ViewHolder>() {
    private var mData: MutableList<Breed>? = null

    fun setData(data: MutableList<Breed>?) {
        mData = data
    }

    fun clear() {
        if (mData != null) {
            mData!!.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBreedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.breed = mData!![position]
        holder.mBinding.executePendingBindings()

        Glide.with(holder.mBinding.image)
            .load(mData!![position].getImageUrl())
            .placeholder(R.drawable.placeholder)
            .circleCrop()
            .into(holder.mBinding.image)

        holder.itemView.setOnClickListener { v: View? ->
            BreedImagesFragment.newInstance(
                v,
                mData!![position]
            )
        }
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    inner class ViewHolder(var mBinding: ItemBreedBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}