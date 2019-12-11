package com.example.inzynierka.Report.ListOfReports;

import android.app.Application;
import android.util.Log;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ListOfReportsViewModel extends AndroidViewModel {
    final String BY_DATE = "byDate";
    final String BY_TITLE = "byTitle";
    final String BY_LOCALIZATION = "byLocalization";
    private String currentOrder = BY_DATE;
    ReportRepository reportRepository;
    List<ReportEntity> listAllReports = new ArrayList<>();

    public ListOfReportsViewModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);

    }
    public LiveData<List<ReportEntity>> getAllReports() {
        return reportRepository.getAllReports();
    }

    public LiveData<List<ReportEntity>> getReportsByTitleOrder() {
        return reportRepository.getReportsByTitleOrder();
    }
    public void deleteReport(ReportEntity reportEntity){
        reportRepository.delete(reportEntity);
    }

    public void sortResults(String order) {
        if (order.equals(BY_TITLE)){
            Collections.sort(listAllReports, new Comparator<ReportEntity>() {
                @Override
                public int compare(final ReportEntity object1, final ReportEntity object2) {
                    return object1.getReportTitle().compareTo(object2.getReportTitle());
                }
            });
        }
        if (order.equals(BY_LOCALIZATION)){
            Log.e("Compare by loc", "Dziala");
            Collections.sort(listAllReports, new Comparator<ReportEntity>() {
                @Override
                public int compare(final ReportEntity object1, final ReportEntity object2) {
                    String reportLocalization1 = object1.getReportLocalization();
                    String reportLozalization2 = object2.getReportLocalization();
                    if(reportLocalization1 == null && reportLozalization2 == null){
                        return 0;
                    }
                    else if (reportLocalization1 == null){
                        return 999;
                    }
                    else if(reportLozalization2 == null){
                        return - 999;
                    }
                    else {
                        return reportLocalization1.compareTo(reportLozalization2);
                    }
                    }
            });
        }
        if (order.equals(BY_DATE)){
            Collections.sort(listAllReports, new Comparator<ReportEntity>() {
                @Override
                public int compare(final ReportEntity object1, final ReportEntity object2) {
                    String reportDate = object1.getReportDate();
                    String reportDate2 = object2.getReportDate();
                    if(reportDate == null && reportDate2 == null){
                        return 0;
                    }
                    else if (reportDate == null){
                        return 999;
                    }
                    else if(reportDate2 == null){
                        return - 999;
                    }
                    return object1.getReportDate().compareTo(object2.getReportDate());
                }
            });
        }
        currentOrder = order;
    }

    public List<ReportEntity> getListAllReports() {
        return listAllReports;
    }

    public void setListAllReports(List<ReportEntity> listAllReports) {
        this.listAllReports = listAllReports;
    }

    public String getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(String currentOrder) {
        this.currentOrder = currentOrder;
    }
}

