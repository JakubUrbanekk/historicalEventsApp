package com.example.inzynierka.Report;

import android.app.Application;
import android.net.Uri;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Photo.PhotoRepository;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class AddReportViewModel extends AndroidViewModel {
    private PhotoRepository photoRepository;
    private List<Uri> photosList;
    private MutableLiveData<List<Uri>> liveDataPhotosList;
    private ReportRepository reportRepository;
    private Uri mainPhotoUri;
    public AddReportViewModel(Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
        reportRepository = new ReportRepository(application);
        photosList = new ArrayList<>();
        liveDataPhotosList = new MutableLiveData<>();
    }
        public void insertPhoto(PhotoEntity photoEntity){
        photoRepository.insert(photoEntity);
}
        public void insertReport(ReportEntity reportEntity){
        reportRepository.insert(reportEntity);
        }

    public MutableLiveData<List<Uri>> getLiveDataPhotosList() {
        return liveDataPhotosList;
    }
    public List<Uri> getPhotosList() {
        return photosList;
    }

    public void addPhoto(Uri uri){
        photosList.add(uri);
        liveDataPhotosList.setValue(photosList);
    }

    public Uri getMainPhotoUri() {
        return mainPhotoUri;
    }

    public void setMainPhotoUri(Uri mainPhotoUri) {
        this.mainPhotoUri = mainPhotoUri;
    }
}

