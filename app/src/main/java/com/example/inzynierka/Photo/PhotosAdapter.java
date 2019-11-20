package com.example.inzynierka.Photo;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.AddReport.AddReportViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    List<Uri> photosList;
    List<PhotoEntity> photoEntities;
    Fragment context;
    ImageView mainPhoto;
    AddReportViewModel reportViewModel;
    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;

    public PhotosAdapter(Fragment context, ImageView mainPhoto, AddReportViewModel reportViewModel, TextInputLayout textInputLayout, TextInputEditText textInputEditText){
        this.photosList = new ArrayList<>();
        this.context = context;
        this.mainPhoto = mainPhoto;
        this.reportViewModel = reportViewModel;
        this.textInputLayout = textInputLayout;
        this.textInputEditText = textInputEditText;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Uri imageUri = photosList.get(position);
        Glide
                .with(context)
                .load(imageUri)
                .centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide
                        .with(context)
                        .load(imageUri)
                        .placeholder(R.drawable.noimge)
                        .centerCrop()
                        .into(mainPhoto);
                PhotoEntity photoEntity = photoEntities.get(position);
                reportViewModel.setMainPhoto(photoEntity);
                if (photoEntity.getPhotoDescription()!=null){
                    textInputEditText.setText(photoEntity.getPhotoDescription());
                }
                else{
                    textInputEditText.getText().clear();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }
    public void setPhotos(List<PhotoEntity> photoEntities){
        this.photoEntities = photoEntities;
        photosList.clear();
        for (PhotoEntity photoEntity: photoEntities){
            photosList.add(photoEntity.getPhotoUri());
        }
        notifyDataSetChanged();
    }

}

