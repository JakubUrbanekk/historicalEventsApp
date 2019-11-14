package com.example.inzynierka.Report;

import android.app.Application;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ListOfReportsViewModel extends AndroidViewModel {
    LiveData<List<ReportEntity>> reportEntityList;
    ReportRepository reportRepository;
    public ListOfReportsViewModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
    }
    public LiveData<List<ReportEntity>> getAllReports (){
        return reportRepository.getAllReports();
    }
}

