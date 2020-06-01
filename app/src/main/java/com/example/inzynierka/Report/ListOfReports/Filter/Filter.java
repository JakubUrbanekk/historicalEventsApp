package com.example.inzynierka.Report.ListOfReports.Filter;

import com.example.inzynierka.CustomDate.CustomData;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.videos.VideoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    List<ReportEntity> allReports;
    List<PhotoEntity> allPhotos;
    List<VideoEntity> allVideos;
    static final List<ReportEntity> LIST_EMPTY_RESULT = new ArrayList<>();
    public Filter(List<ReportEntity> reports, List<PhotoEntity> photoEntities) {
        allReports = reports;
        allPhotos = photoEntities;
    }

    public Filter() {
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

    public int getReportsMaxPhotosAmount(){
        List<ReportEntity> result = new ArrayList<>(allReports);

        return result.stream()
                .mapToInt(reportEntity -> getPhotosFromReport(reportEntity).size())
                .max()
                .orElseGet(()->-1);
    }

    public List<ReportEntity> getReportsWithMaxPhotosAmount(int amount) {
        List<ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> getPhotosFromReport(reportEntity).size() <= amount)
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
                .filter(reportEntity -> reportEntity.getReportLocalization().toLowerCase().contains(finalLocalization))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> intersectResults(List<List<ReportEntity>> lists) {
        lists = lists.stream()
                .filter(reportEntityList -> reportEntityList!=null && !reportEntityList.isEmpty())
                .collect(Collectors.toList());

        if (lists.isEmpty()){
            return new ArrayList<>();
        }

        List<ReportEntity> result = new ArrayList<>(lists.get(0));
        for (List<ReportEntity> list : lists){
            result.retainAll(list);
        }
        return result;
    }

    public List<ReportEntity> getReportsFromPastDays(int days) {
        List <ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> reportEntity.getDate()!=null)
                .filter(reportEntity -> CustomData.checkIfDateIsFromPastDays(days, reportEntity.getDate()))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsFromPastMonths(int months) {
        List <ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> reportEntity.getDate()!=null)
                .filter(reportEntity -> CustomData.checkIfDateIsFromPastMonths(months, reportEntity.getDate()))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getAllReports() {
        return allReports;
    }

    public void setAllReports(List<ReportEntity> allReports) {
        this.allReports = allReports;
    }

    public void setAllPhotos(List<PhotoEntity> allPhotos) {
        this.allPhotos = allPhotos;
    }

    public void setAllVideos(List<VideoEntity> allVideos) {
        this.allVideos = allVideos;
    }

    public int getReportsMaxVideosAmount() {
        List<ReportEntity> result = new ArrayList<>(allReports);

        return result.stream()
                .mapToInt(reportEntity -> getVideosFromReport(reportEntity).size())
                .max()
                .orElseGet(()->-1);
    }

    public List<VideoEntity> getVideosFromReport(ReportEntity reportEntity) {
        int reportId = reportEntity.getReportId();
        List<VideoEntity> result = new ArrayList<>(allVideos);
        result = result.stream()
                .filter(videoEntity -> videoEntity.getReportIdForVideo()==reportId)
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsWithMinimumVideosAmount(int iMinVideos) {
        List<ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> getVideosFromReport(reportEntity).size() >= iMinVideos)
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsWithMaximumVideosAmount(int iMaxVideos) {
        List<ReportEntity> result = new ArrayList<>(allReports);
        result = result.stream()
                .filter(reportEntity -> getVideosFromReport(reportEntity).size() <= iMaxVideos)
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportCategory(String category) {
        category = category.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalCategory = category;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getCategory() != null)
                .filter(reportEntity -> reportEntity.getCategory().toLowerCase().contains(finalCategory))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportEpoka(String epoka) {
        epoka = epoka.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalepoka = epoka;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getEpoka() != null)
                .filter(reportEntity -> reportEntity.getEpoka().toLowerCase().contains(finalepoka))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportVehicle(String vehicle) {
        vehicle = vehicle.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalLocalization = vehicle;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getVehicle() != null)
                .filter(reportEntity -> reportEntity.getVehicle().toLowerCase().contains(finalLocalization))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportCloth(String cloth) {
        cloth = cloth.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalcloth = cloth;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getCloth() != null)
                .filter(reportEntity -> reportEntity.getCloth().toLowerCase().contains(finalcloth))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportWeapon(String weapon) {
        weapon = weapon.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalWeapon = weapon;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getWeapon() != null)
                .filter(reportEntity -> reportEntity.getWeapon().toLowerCase().contains(finalWeapon))
                .collect(Collectors.toList());
        return result;
    }

    public List<ReportEntity> getReportsByReportAccessory(String accessory) {
        accessory = accessory.toLowerCase();
        List<ReportEntity> result = new ArrayList<>(allReports);
        String finalAccessory = accessory;
        result = result.stream()
                .filter(reportEntity -> reportEntity.getAccessory() != null)
                .filter(reportEntity -> reportEntity.getAccessory().toLowerCase().contains(finalAccessory))
                .collect(Collectors.toList());
        return result;
    }
}
