package com.example.inzynierka.Report.AddReport;

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
    private List<PhotoEntity> photosList;
    private MutableLiveData<List<PhotoEntity>> liveDataPhotosList;
    private ReportRepository reportRepository;
    private PhotoEntity mainPhotoEntity;
    private String reportTitle;
    private String reportDescription;
    private String reportDate;
    private String reportLocalization;
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

    public MutableLiveData<List<PhotoEntity>> getLiveDataPhotosList() {
        return liveDataPhotosList;
    }
    public List<PhotoEntity> getPhotosList() {
        return photosList;
    }

    public PhotoEntity getMainPhotoEntity() {
        return mainPhotoEntity;
    }

    public void addPhoto(PhotoEntity photoEntity){
        photosList.add(photoEntity);
        liveDataPhotosList.setValue(photosList);
    }

    public PhotoEntity getMainPhotoUri() {
        return mainPhotoEntity ;
    }

    public void setMainPhoto(PhotoEntity mainPhotoEntity) {
        this.mainPhotoEntity = mainPhotoEntity;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportLocalization() {
        return reportLocalization;
    }

    public void setReportLocalization(String reportLocalization) {
        this.reportLocalization = reportLocalization;
    }
}

