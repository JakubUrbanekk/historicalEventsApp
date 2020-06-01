package com.example.inzynierka.Report.EditReport;

import android.app.Application;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Photo.PhotoRepository;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.Database.videos.VideoRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import lombok.Getter;

@Getter
public class EditReportModel extends AndroidViewModel {
    ReportEntity currentReport;
    ReportRepository reportRepository;
    PhotoRepository photoRepository;
    VideoRepository videoRepository;
    List<PhotoEntity> photosCurrent;
    List<VideoEntity> videosCurrent;
    public EditReportModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
        photoRepository = new PhotoRepository(application);
        videoRepository = new VideoRepository(application);
    }

    public List<PhotoEntity> getPhotosCurrent() {
        if(photosCurrent==null){
            return new ArrayList<>();
        }
        return photosCurrent;
    }


    public void setCurrentReport(ReportEntity currentReport) {
        this.currentReport = currentReport;
    }

    public void updateReport(){
        reportRepository.update(currentReport);
    }
    public void updatePhoto(PhotoEntity photoEntity){
        photoRepository.update(photoEntity);
    }

    public LiveData<ReportEntity> getReportById(Integer id){
        return reportRepository.getReportById(id);
    }


    public LiveData<List<PhotoEntity>> getPhotosFromReport(int id){
        return photoRepository.getPhotosFromReportById(id);
    }

    public LiveData<List<VideoEntity>> getVideosFromReport(int id){
        return videoRepository.getVideosFromReportById(id);
    }

    public long insertPhoto(PhotoEntity photoEntity){
        return photoRepository.insert(photoEntity);
    }

    public void setPhotosCurrent(List<PhotoEntity> photosCurrent) {
        this.photosCurrent = photosCurrent;
    }

    public List<VideoEntity> getVideosCurrent() {
        if(videosCurrent == null){
            return new ArrayList<>();
        }
        return videosCurrent;
    }

    public void setVideosCurrent(List<VideoEntity> videosCurrent) {
        this.videosCurrent = videosCurrent;
    }

    public void insertVideo(VideoEntity videoAfterUpdate) {
        videoRepository.insert(videoAfterUpdate);
    }
    public void updateVideo(VideoEntity videoEntity){
        videoRepository.update(videoEntity);
    }
    public void deleteVideo(VideoEntity videoEntity){
        videoRepository.delete(videoEntity);
    }
    public void deletePhoto(PhotoEntity photoEntity){
        photoRepository.delete(photoEntity);
    }
}
