package com.example.inzynierka.Report.ListOfReports;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    List<ReportEntity> allReports;
    List<PhotoEntity> allPhotos;
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
        allReports.stream().forEach(reportEntity -> System.out.println(getPhotosFromReport(reportEntity).size() + reportEntity.toString()));
        System.out.println("amount "+ amount);
        List<ReportEntity> result = allReports.stream()
                .filter(reportEntity -> getPhotosFromReport(reportEntity).size() >= amount)
                .collect(Collectors.toList());
        result.forEach(reportEntity -> System.out.println("Nowa lista" + reportEntity.toString()));
        return result;
    }
}
