package com.example.inzynierka.Report.ListOfReports;

import com.example.inzynierka.CustomData;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    List<ReportEntity> allReports;
    List<PhotoEntity> allPhotos;
    CustomData currentDate;
    public Filter(List<ReportEntity> reports, List<PhotoEntity> photoEntities) {
        allReports = reports;
        allPhotos = photoEntities;
    }

    public List<PhotoEntity> getPhotosFromReport(ReportEntity reportEntity) {
        int reportId = reportEntity.getReportId();
        List<PhotoEntity> result = new ArrayList<>(allPhotos);
        result = result.stream()
                .filter(photoEntity -> photoEntity.getReportId()==reportId)
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsWithMinimumPhotosAmount(int amount) {
        List<ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> getPhotosFromReport(reportEntity).size() >= amount)
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsFromPastYears(int years) {
        List <ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> reportEntity.getDate()!=null)
                .filter(reportEntity -> CustomData.checkIfDateIsFromPastYears(years, reportEntity.getDate()))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsFromSelectedYear(int year) {
        List <ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> reportEntity.getDate() != null)
                .filter(reportEntity -> reportEntity.getDate().getYear().equals(Integer.toString(year)))
                .collect(Collectors.toList());
        return result;
    }
    public  List<ReportEntity> getReportsFromSelectedYearAndMonth(int year, int month){
        List <ReportEntity> result = new ArrayList<>(getReportsFromSelectedYear(year));
        result = result.stream()
                .filter(reportEntity -> Integer.parseInt(reportEntity.getDate().getMonth()) == month)
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportTitle(String titlePart) {
        titlePart = titlePart.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalTitlePart = titlePart;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getReportTitle() != null)
                .filter(reportEntity -> reportEntity.getReportTitle().toLowerCase().contains(finalTitlePart))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportLocalization(String localization) {
        localization = localization.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalLocalization = localization;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getReportTitle() != null)
                .filter(reportEntity -> reportEntity.getReportTitle().toLowerCase().contains(finalLocalization))
                .collect(Collectors.toList());
        return result;
    }
}
