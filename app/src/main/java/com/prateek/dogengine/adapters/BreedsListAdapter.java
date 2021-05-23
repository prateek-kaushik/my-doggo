package com.prateek.dogengine.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prateek.dogengine.BreedImagesFragment;
import com.prateek.dogengine.R;
import com.prateek.dogengine.data.Breed;
import com.prateek.dogengine.databinding.ItemBreedBinding;

import java.util.List;

public class BreedsListAdapter extends RecyclerView.Adapter<BreedsListAdapter.ViewHolder> {

    private List<Breed> mData;

    public void setData(List<Breed> data) {
        mData = data;
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBreedBinding binding = ItemBreedBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mBinding.setBreed(mData.get(position));
        holder.mBinding.executePendingBindings();

        Glide.with(holder.mBinding.image)
                .load(mData.get(position).getImageUrl())
                .placeholder(R.drawable.placeholder)
                .circleCrop()
                .into(holder.mBinding.image);

        holder.itemView.setOnClickListener(v -> {
            BreedImagesFragment.newInstance(v, mData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemBreedBinding mBinding;

        public ViewHolder(ItemBreedBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
