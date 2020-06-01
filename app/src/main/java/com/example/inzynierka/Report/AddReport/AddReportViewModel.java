package com.example.inzynierka.Report.AddReport;

import android.app.Application;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Photo.PhotoRepository;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.Database.videos.VideoRepository;
import com.example.inzynierka.Report.ModelWithDatabaseInsertMethods;

import androidx.lifecycle.AndroidViewModel;

public class AddReportViewModel extends AndroidViewModel implements ModelWithDatabaseInsertMethods {
    private PhotoRepository photoRepository;
    private ReportRepository reportRepository;
    private VideoRepository videoRepository;
    public AddReportViewModel(Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        photoRepository = new PhotoRepository(application);
        reportRepository = new ReportRepository(application);
    }
    public long insertPhoto(PhotoEntity photoEntity){
        return photoRepository.insert(photoEntity);
}

    public long insertReport(ReportEntity reportEntity){
        return reportRepository.insert(reportEntity);
        }

    public long insertVideo(VideoEntity videoEntity) {
        return videoRepository.insert(videoEntity);
    }
}

