package com.example.inzynierka.Report.EditReport;

import android.app.Application;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Photo.PhotoRepository;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class EditReportModel extends AndroidViewModel {
    ReportEntity currentReport;
    ReportRepository reportRepository;
    PhotoRepository photoRepository;

    public EditReportModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
        photoRepository = new PhotoRepository(application);
    }
}
