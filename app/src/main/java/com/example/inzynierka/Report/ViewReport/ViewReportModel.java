package com.example.inzynierka.Report.ViewReport;

import android.app.Application;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Photo.PhotoRepository;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewReportModel extends AndroidViewModel {
    ReportEntity currentReport;
    ReportRepository reportRepository;
    PhotoRepository photoRepository;

    public ViewReportModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
        photoRepository = new PhotoRepository(application);
    }

    public LiveData<ReportEntity> getReportById(Integer id){
        return reportRepository.getReportById(id);
    }
    public List<PhotoEntity> getPhotosFromReport(){
        return photoRepository.getPhotosFromReportById(currentReport.getReportId()).getValue();
    }

    public ReportEntity getCurrentReport() {
        return currentReport;
    }
    public void setCurrentReport(ReportEntity currentReport) {
        this.currentReport = currentReport;
    }
}
