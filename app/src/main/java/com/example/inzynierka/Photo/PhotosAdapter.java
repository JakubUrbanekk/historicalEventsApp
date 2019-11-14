package com.example.inzynierka.Photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.AddReportViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    List<Uri> photosList;
    Context context;
    ImageView mainPhoto;
    AddReportViewModel reportViewModel;
    TextInputLayout textInputLayout;

    public PhotosAdapter(Context context, ImageView mainPhoto, AddReportViewModel reportViewModel, TextInputLayout textInputLayout){
        this.photosList = new ArrayList<>();
        this.context = context;
        this.mainPhoto = mainPhoto;
        this.reportViewModel = reportViewModel;
        this.textInputLayout = textInputLayout;
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
        final Uri imageUri = photosList.get(position);
        Glide
                .with(context)
                .load(imageUri)
                .centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mainPhotoVisibility = mainPhoto.getVisibility();
                if(mainPhotoVisibility==8){
                    mainPhoto.setVisibility(View.VISIBLE);
                    textInputLayout.setVisibility(View.VISIBLE);
                }
                Glide
                        .with(context)
                        .load(imageUri)
                        .placeholder(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(mainPhoto);
                Log.i("MainPhoto", "main photo uri" +imageUri + " visibility " +mainPhoto.getVisibility());
                reportViewModel.setMainPhotoUri(imageUri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }
    public void setPhotos(List<Uri> photoUris){
        photosList = photoUris;
        notifyDataSetChanged();
    }

}

