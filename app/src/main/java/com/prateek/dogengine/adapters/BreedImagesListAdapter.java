package com.prateek.dogengine.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prateek.dogengine.R;
import com.prateek.dogengine.data.BreedImage;

public class BreedImagesListAdapter extends PagedListAdapter<BreedImage, BreedImagesListAdapter.ViewHolder> {
    private static DiffUtil.ItemCallback<BreedImage> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BreedImage>() {
                @Override
                public boolean areItemsTheSame(BreedImage oldItem, BreedImage newItem) {
                    return oldItem.getMId() == newItem.getMId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull BreedImage oldItem, @NonNull BreedImage newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public BreedImagesListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(getItem(position).getMUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.mImage);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
        }
    }
}
