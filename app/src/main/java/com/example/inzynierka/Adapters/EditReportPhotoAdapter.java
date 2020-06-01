package com.example.inzynierka.Adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class EditReportPhotoAdapter extends RecyclerView.Adapter<EditReportPhotoAdapter.ViewHolder> {
    ReportEntity currentReport;
    List<PhotoEntity> photoEntities;
    Fragment context;
    ImageView mainPhoto;
    TextInputEditText photoDescriptionEditText;
    public EditReportPhotoAdapter(Fragment context, ReportEntity reportEntity, ImageView mainPhoto, TextInputEditText photoDescription){
        this.context = context;
        this.currentReport = reportEntity;
        this.photoEntities = new ArrayList<>();
        this.mainPhoto = mainPhoto;
        this.photoDescriptionEditText = photoDescription;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_photo_item, parent, false);
        return new  EditReportPhotoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotoEntity photoEntity = photoEntities.get(position);
        Uri uri = photoEntity.getPhotoUri();
        Glide
                .with(context)
                .load(uri)
                .centerCrop()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentReport.setMainPhoto(photoEntity);
                Log.e("On Click", currentReport.toString());
                Glide
                        .with(context)
                        .load(currentReport.getMainPhoto())
                        .centerCrop()
                        .into(mainPhoto);

                String photoDescription = photoEntity.getPhotoDescription();
                if(photoDescription==null){
                    photoDescriptionEditText.getText().clear();
                }
                else {
                    photoDescriptionEditText.setText(photoDescription);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewReportItemPhoto);
        }
    }
    public void setPhotos(List<PhotoEntity> photoEntities){
        this.photoEntities = photoEntities;
        notifyDataSetChanged();
    }
}
