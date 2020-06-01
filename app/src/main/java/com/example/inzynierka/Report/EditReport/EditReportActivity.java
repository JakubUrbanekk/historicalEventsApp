package com.example.inzynierka.Report.EditReport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.EditableReportActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

@lombok.extern.java.Log
public class EditReportActivity extends EditableReportActivity {
    EditReportModel editViewModel;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editViewModel = ViewModelProviders.of(this).get(EditReportModel.class);
        Intent intent = getIntent();
        int currentReportId = intent.getIntExtra("currentReport", 0);
        title = (TextView) findViewById(R.id.reportTitleTV);
        title.setText("Edytuj relację");
        addReport.setImageResource(R.drawable.ic_save_black_24dp);
        editViewModel.getReportById(currentReportId).observe(this, new Observer<ReportEntity>() {
            @Override
            public void onChanged(ReportEntity reportEntity) {
                editViewModel.setCurrentReport(reportEntity);
                etLocalization.setText(reportEntity.getReportLocalization());
                etTitle.setText(reportEntity.getReportTitle());
                tvDate.setText(reportEntity.getReportDate());
                etDescription.setText(reportEntity.getReportDescription());
            }
        });
        editViewModel.getPhotosFromReport(currentReportId).observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(List<PhotoEntity> photoEntities) {
                Log.e("Edit report activity", "Photo size " + photoEntities.size());
                if (!photoEntities.isEmpty()) {
                    aPhotoAdapter.setPhotos(photoEntities);
                    photoPane.setVisibility(View.VISIBLE);
                    if(editViewModel.getPhotosCurrent()==null) {
                        editViewModel.setPhotosCurrent(photoEntities);
                    }
                    viewModel.setPhotoEntities(photoEntities);
                }
            }
        });
        editViewModel.getVideosFromReport(currentReportId).observe(this, new Observer<List<VideoEntity>>() {
            @Override
            public void onChanged(List<VideoEntity> videoEntities) {
                if (!videoEntities.isEmpty()) {
                    aVideoAdapter.setVideos(videoEntities);
                    videoPane.setVisibility(View.VISIBLE);
                    if(editViewModel.getPhotosCurrent()==null) {
                        editViewModel.setVideosCurrent(videoEntities);
                    }
                    videoModel.setVideoEntities(videoEntities);
                }
            }
        });
    }


    @Override
    protected int setAlertDialogMessage() {
        return R.string.editReportAlertDialog;
    }

    @Override
    protected int setAlertDialogTitle() {
        return R.string.editReportTitle;
    }

    @Override
    public void updateDatabase() {
        editViewModel.getCurrentReport().setCategory(getCategorySpinnerValue());
        editViewModel.getCurrentReport().setEpoka(getEpokaSpinnerValue());
        editViewModel.getCurrentReport().setAccessory(getAccessorySpinnerValue());
        editViewModel.getCurrentReport().setCloth(getClothSpinnerValue());
        editViewModel.getCurrentReport().setWeapon(getWeaponSpinnerValue());
        editViewModel.getCurrentReport().setVehicle(getVehicleSpinnerValue());
        editViewModel.updateReport();
        List<PhotoEntity> photosFromActivity = viewModel.getPhotosList();
        List<VideoEntity> videosFromActivity = videoModel.getVideoList();
        List<PhotoEntity> photosBeforeUpdate = editViewModel.getPhotosCurrent();
        List<VideoEntity> videosBeforeUpdate = editViewModel.getVideosCurrent();
        Executor mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (PhotoEntity photoEntity : photosBeforeUpdate) {
                    Log.e("Edit activity", "Photo " + photoEntity.toString());
                    if (!photosFromActivity.contains(photoEntity)) {
                        editViewModel.deletePhoto(photoEntity);
                    }
                }
                for (VideoEntity videoEntity : videosBeforeUpdate) {
                    if (!videosFromActivity.contains(videoEntity)) {
                        editViewModel.deleteVideo(videoEntity);
                    }
                }

                for (PhotoEntity photoAfterUpdate : photosFromActivity) {
                    if (photoAfterUpdate.getReportId() == null) {
                        photoAfterUpdate.setReportId(editViewModel.currentReport.getReportId());
                        editViewModel.insertPhoto(photoAfterUpdate);
                    } else if (!(photoAfterUpdate.getReportId() == null)) {
                        editViewModel.updatePhoto(photoAfterUpdate);
                    }

                }
                for (VideoEntity videoAfterUpdate : videosFromActivity) {
                    if (videoAfterUpdate.getReportIdForVideo() == null) {
                        videoAfterUpdate.setReportIdForVideo(editViewModel.currentReport.getReportId());
                        editViewModel.insertVideo(videoAfterUpdate);
                    } else if (!(videoAfterUpdate.getReportIdForVideo() == null)) {
                        editViewModel.updateVideo(videoAfterUpdate);
                    }
                }
            }
        });
}
}
