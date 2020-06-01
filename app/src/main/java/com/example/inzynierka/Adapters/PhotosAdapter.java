package com.example.inzynierka.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    List<Uri> photosList;
    private String id;
    Context context;
    RecyclerViewItemClickListener listener;

    public PhotosAdapter(Context context, RecyclerViewItemClickListener listener){
        this.photosList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }
    public PhotosAdapter(Context context, RecyclerViewItemClickListener listener, String id){
        this.photosList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.id = id;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageViewReportItemPhoto);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.recyclerViewListClicked(v,this.getAdapterPosition(), id);
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_photo_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Uri imageUri = photosList.get(position);
        Glide
                .with(context)
                .load(imageUri)
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public void setPhotos(List<PhotoEntity> photoEntities){
        photosList.clear();
        for (PhotoEntity photoEntity: photoEntities){
            photosList.add(photoEntity.getPhotoUri());
        }
        notifyDataSetChanged();
    }

    public void setVideos(List<VideoEntity> videoEntities){
        photosList.clear();
        for (VideoEntity photoEntity: videoEntities){
            photosList.add(photoEntity.getVideoUri());
        }
        notifyDataSetChanged();
    }

}

