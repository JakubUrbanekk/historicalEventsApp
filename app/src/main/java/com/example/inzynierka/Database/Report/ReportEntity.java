package com.example.inzynierka.Database.Report;

import android.net.Uri;

import com.example.inzynierka.CustomData;
import com.example.inzynierka.Database.Photo.PhotoEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reports")
public class ReportEntity {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    Integer reportId;
    String reportDescription;
    CustomData reportDate;
    @Nullable
    PhotoEntity mainPhoto;
    String reportLocalization;
    String reportTitle;

    public ReportEntity() {
    }
    public String getReportLocalization() {
        return reportLocalization;
    }
    public void setReportLocalization(String reportLocalization) {
        this.reportLocalization = reportLocalization;
    }
    public String getReportTitle() {
        return reportTitle;
    }
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }
    @NonNull
    public Integer getReportId() {
        return reportId;
    }
    public void setReportId(@NonNull Integer reportId) {
        this.reportId = reportId;
    }
    public String getReportDescription() {
        return reportDescription;
    }
    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }
    public String getReportDate() {
        return reportDate.toString();
    }
    public void setReportDate(String reportDate) {
        CustomData customData = new CustomData(reportDate);
        this.reportDate = customData;
    }
    public Uri getMainPhoto() {
        return mainPhoto.getPhotoUri();
    }
    public PhotoEntity getMainPhotoEntity(){
        return mainPhoto;
    }
    public void setMainPhoto(PhotoEntity photoEntity){
        this.mainPhoto = photoEntity;
    }
    public CustomData getDate(){
        return reportDate;
    }
    @Override
    public String toString() {
        return "ReportEntity{" +
                "reportId=" + reportId +
                ", reportDescription='" + reportDescription + '\'' +
                ", reportDate=" + reportDate +
                ", mainPhoto=" + mainPhoto +
                ", reportLocalization='" + reportLocalization + '\'' +
                ", reportTitle='" + reportTitle + '\'' +
                '}';
    }
}
