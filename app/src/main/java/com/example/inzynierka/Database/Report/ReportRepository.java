package com.example.inzynierka.Database.Report;

import android.app.Application;
import android.os.AsyncTask;

import com.example.inzynierka.Database.AppRoomDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ReportRepository {
    private ReportDao reportDao;

    public ReportRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        reportDao = db.reportDao();
    }

    public long insert(final ReportEntity reportEntity) {
        return reportDao.insert(reportEntity);

    }
    public LiveData<List<ReportEntity>> getAllReports(){
        return reportDao.getAllReports();
    }
    public LiveData<List<ReportEntity>> getReportsByTitleOrder(){
        return  reportDao.getReportsByTitleOrder();

    }
    public void update(ReportEntity reportEntity){
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                reportDao.update(reportEntity);
            }
        });
    }
    public void delete(ReportEntity reportEntity){
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                reportDao.delete(reportEntity);
            }
        });
    }
    public LiveData<ReportEntity> getReportById(Integer id){
        return reportDao.getReportById(id);
    }
}