package com.example.inzynierka.Report.EditReport;

import android.app.Application;
import android.net.Uri;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Photo.PhotoRepository;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;
import com.example.inzynierka.Report.ViewModelMainPhoto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EditReportModel extends AndroidViewModel implements ViewModelMainPhoto {
    ReportEntity currentReport;
    ReportRepository reportRepository;
    PhotoRepository photoRepository;
    int currentReportId;
    MutableLiveData<List<PhotoEntity>> photosCurrent;
    List<PhotoEntity> photosToDelete;
    List<PhotoEntity> photosToInsert;
    public EditReportModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
        photoRepository = new PhotoRepository(application);
        photosToDelete = new ArrayList<>();
        photosToInsert = new ArrayList<>();
        photosCurrent = new MutableLiveData<>();
    }

    public MutableLiveData<List<PhotoEntity>> getPhotosCurrent() {
        return photosCurrent;
    }
    public void addToCurrentPhotos(PhotoEntity currentPhotos) {
        this.photosCurrent.getValue().add(currentPhotos);
        photosCurrent.setValue(photosCurrent.getValue());
    }
    public void deleteFromCurrentPhotos(PhotoEntity photoToDelete){
        for (PhotoEntity photoEntity : this.photosCurrent.getValue())
        if(photoEntity.getPhotoUri().equals(photoToDelete.getPhotoUri())) {
            this.photosCurrent.getValue().remove(photoEntity);
            break;
        }
        getPhotosToInsert().remove(photoToDelete);
        photosCurrent.setValue(photosCurrent.getValue());
    }
    public int currentPhotosSize(){
        return photosCurrent.getValue().size();
    }
    public PhotoEntity firstPhotoFromList(){
        return photosCurrent.getValue().get(0);
    }
    public void setCurrentReport(ReportEntity currentReport) {
        this.currentReport = currentReport;
    }
    public int getCurrentReportId() {
        return currentReportId;
    }
    public void setCurrentReportId(int currentReportId) {
        this.currentReportId = currentReportId;
    }
    public ReportEntity getCurrentReport() {
        return currentReport;
    }
    public void updatePhoto(PhotoEntity photoEntity){
        photoRepository.update(photoEntity);
    }
    public void updateReport(){
        reportRepository.update(currentReport);
    }
    public void deletePhoto(PhotoEntity photoEntity){
        photoRepository.delete(photoEntity);
    }
    public void deleteReport(ReportEntity reportEntity){
        reportRepository.delete(reportEntity);
    }
    public PhotoEntity getPhotoById(Integer photoId){
        return photoRepository.getPhotoById(photoId);
    }
    public LiveData<ReportEntity> getReportById(Integer id){
        return reportRepository.getReportById(id);
    }
    public LiveData<List<ReportEntity>> getAllReports(){
        return reportRepository.getAllReports();
    }
    public LiveData<List<PhotoEntity>> getPhotosFromReport(){
        return photoRepository.getPhotosFromReportById(currentReport.getReportId());
    }
    public List<PhotoEntity> getPhotosToDelete() {
        return photosToDelete;
    }
    public List<PhotoEntity> getPhotosToInsert() {
        return photosToInsert;
    }
    public void setPhotosToInsert(List<PhotoEntity> photosToInsert) {
        this.photosToInsert = photosToInsert;
    }
    public void setPhotosToDelete(List<PhotoEntity> photosToDelete) {
        this.photosToDelete = photosToDelete;
    }
    public void insertPhoto(PhotoEntity photoEntity){
        photoRepository.insert(photoEntity);
    }
    public PhotoEntity getMainPhotoFromCurrentReport(){
        return currentReport.getMainPhotoEntity();
    }
    @Override
    public void setMainPhoto(PhotoEntity photoEntity) {
        currentReport.setMainPhoto(photoEntity);
    }
    public LiveData<PhotoEntity> getPhotoByUriFromCurrentReport(Uri uri){
        return photoRepository.getPhotoByUri(uri.toString(),getCurrentReportId());
    }
    public String getMainPhotoDescription(){
        String mainPhotoUri = currentReport.getMainPhoto().toString();
        for (PhotoEntity photoEntity: photosCurrent.getValue()){
            if(photoEntity.getPhotoUri().equals(mainPhotoUri));
            return photoEntity.getPhotoDescription();
        }
        return "";
    }
}
