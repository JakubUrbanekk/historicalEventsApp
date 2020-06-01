package com.example.inzynierka.Report.ListOfReports.Filter;

import android.app.Application;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Photo.PhotoRepository;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.Database.videos.VideoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class FilterModel extends AndroidViewModel {
    String titleText;
    ReportRepository reportRepository;
    PhotoRepository photoRepository;
    VideoRepository videoRepository;
    List<ReportEntity> allReports = new ArrayList<>();
    List<PhotoEntity> allPhotos = new ArrayList<>();
    List<VideoEntity> allVideos = new ArrayList<>();
    DataForFilterActivityExtractor dataForFilterActivityExtractor;
    Filter filter;
    FilterCache filterCache;

    public FilterModel(@NonNull Application application) {
        super(application);
        reportRepository = new ReportRepository(application);
        photoRepository = new PhotoRepository(application);
        videoRepository = new VideoRepository(application);
        filterCache = new FilterCache();
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public List<ReportEntity> getAllReports() {
        return allReports;
    }

    public List<VideoEntity> getAllVideos() {
        return allVideos;
    }

    public void setAllReports(List<ReportEntity> allReports) {
        this.allReports = allReports;
    }

    public List<PhotoEntity> getAllPhotos() {
        return allPhotos;
    }

    public void setAllPhotos(List<PhotoEntity> allPhotos) {
        this.allPhotos = allPhotos;
    }

    public void setAllVideos(List<VideoEntity> allVideos){
        this.allVideos = allVideos;
    }

    public DataForFilterActivityExtractor getDataForFilterActivityExtractor() {
        return dataForFilterActivityExtractor;
    }

    List<String> getTextForDateSpinner(){
        return dataForFilterActivityExtractor.getTextForDateSpinner();
    }

    public void setFilter(List<ReportEntity> allReports, List<PhotoEntity> allPhotos, List<VideoEntity> allVideos) {
        if(filter == null){
            filter = new Filter();
        }
        if(allReports!=null){
            filter.setAllReports(allReports);
        }
        if(allPhotos!=null){
            filter.setAllPhotos(allPhotos);
        }
        if(allVideos!=null){
            filter.setAllVideos(allVideos);
        }

        dataForFilterActivityExtractor = new DataForFilterActivityExtractor(allReports);
    }

    public List<String> getReportsLocalizationsForSpinner() {
        return dataForFilterActivityExtractor.getReportsLocalizations();
    }
    public Set<String> getReportsCategoryForSpinner() {
        return dataForFilterActivityExtractor.getReportCategories();
    }
    public Set<String> getReportsEpokaForSpinner() {
        return dataForFilterActivityExtractor.getReportEpoka();
    }
    public Set<String> getReportsClothsForSpinner() {
        return dataForFilterActivityExtractor.getReportClothes();
    }
    public Set<String> getReportsWeaponssForSpinner() {
        return dataForFilterActivityExtractor.getReportWeapons();
    }
    public Set<String> getReportsAccessoryForSpinner() {
        return dataForFilterActivityExtractor.getReportAccessories();
    }
    public Set<String> getReportsVehiclesForSpinner() {
        return dataForFilterActivityExtractor.getReportVehicles();
    }
    public List<String> getReportsYears() {
        return dataForFilterActivityExtractor.getReportsYears();
    }

    public List<String> getReportsMonths(String year) {
        return dataForFilterActivityExtractor.getMonthsFromSelectedYear(year);
    }

    public Filter getFilter() {
        return filter;
    }

    public int getMaxPhotosAmount(){
        return filter.getReportsMaxPhotosAmount();
    }
    public int getMaxVideosAmount(){
        return filter.getReportsMaxVideosAmount();
    }
}
