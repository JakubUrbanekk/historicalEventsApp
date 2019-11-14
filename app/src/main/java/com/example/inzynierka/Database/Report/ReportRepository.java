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

    public void insert(ReportEntity reportEntity) {
        new insertAsyncTask(reportDao).execute(reportEntity);
    }
    public LiveData<List<ReportEntity>> getAllReports(){
        return reportDao.getAllReports();
    }
    private static class insertAsyncTask extends AsyncTask<ReportEntity, Void, Void> {

        private ReportDao asyncReportEntity;

        insertAsyncTask(ReportDao dao) {
            asyncReportEntity = dao;
        }

        @Override
        protected Void doInBackground(final ReportEntity... params) {
            asyncReportEntity.insert(params[0]);
            return null;
        }

    }
}