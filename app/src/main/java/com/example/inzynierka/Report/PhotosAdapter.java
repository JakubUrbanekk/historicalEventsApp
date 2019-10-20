package com.example.inzynierka.Report;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.R;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    List<Uri> photosList;
    Context context;
    ImageView mainPhoto;

    public PhotosAdapter(List<Uri> photosList, Context context, ImageView mainPhoto){
        this.photosList=photosList;
        this.context=context;
        this.mainPhoto=mainPhoto;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageViewReportItemPhoto);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_photo_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Uri imageUri = photosList.get(position);
        Log.e("APP ", imageUri+"");
        Glide
                .with(context)
                .load(imageUri)
                .centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) holder.imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                mainPhoto.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

}

