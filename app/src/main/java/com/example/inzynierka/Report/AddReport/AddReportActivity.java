package com.example.inzynierka.Report.AddReport;

import android.os.Bundle;
import android.util.Log;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.EditableReportActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.ViewModelProviders;


public class AddReportActivity extends EditableReportActivity {
    final static String TAG = AddReportActivity.class.getName();
    private AddReportViewModel addReportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLastLocation();
        addReportViewModel = ViewModelProviders.of(this).get(AddReportViewModel.class);
    }

    @Override
    protected int setAlertDialogMessage() {
        return R.string.addReportAlertDialog;
    }

    @Override
    protected int setAlertDialogTitle() {
        return R.string.addReportTitle;
    }

    @Override
    protected void updateDatabase() {
        ReportEntity reportEntity = new ReportEntity();

        PhotoEntity mainPhoto = getMainPhotoIfExists();
        reportEntity.setReportData(getReportTitle(), getReportDescription(), getReportLocalization(), getReportDate(), mainPhoto);

        reportEntity.setCategory(getCategorySpinnerValue());
        reportEntity.setEpoka(getEpokaSpinnerValue());
        reportEntity.setAccessory(getAccessorySpinnerValue());
        reportEntity.setCloth(getClothSpinnerValue());
        reportEntity.setWeapon(getWeaponSpinnerValue());
        reportEntity.setVehicle(getVehicleSpinnerValue());

        List<PhotoEntity> photosList = viewModel.getPhotosList();
        List<VideoEntity> videosList = videoModel.getVideoList();
        Executor mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long reportId = addReportViewModel.insertReport(reportEntity);
                if (photosList!= null && !photosList.isEmpty()) {
                    for (PhotoEntity photoEntity : photosList) {
                        Log.i(TAG, "Photo " + photoEntity);
                        photoEntity.setReportId((int) (long) (reportId));
                        addReportViewModel.insertPhoto(photoEntity);
                    }
                }
                if(videosList!= null && !videosList.isEmpty()){
                    for(VideoEntity videoEntity : videosList){
                        videoEntity.setReportIdForVideo((int)(long) (reportId));
                        addReportViewModel.insertVideo(videoEntity);
                    }
                }
            }
        });
    }

    private PhotoEntity getMainPhotoIfExists() {
        return viewModel.getPhotosList() != null && !viewModel.getPhotosList().isEmpty() ? viewModel.getPhotosList().get(0) : null;
    }

}

