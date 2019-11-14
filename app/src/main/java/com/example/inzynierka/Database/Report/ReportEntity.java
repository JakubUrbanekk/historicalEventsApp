package com.example.inzynierka.Database.Report;

import android.net.Uri;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reports")
public class ReportEntity {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    Integer reportId;
    Integer eventId;
    String reportDescription;
    Date reportDate;
    Uri mainPhotoUri;

    @NonNull
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(@NonNull Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Uri getMainPhoto() {
        return mainPhotoUri;
    }
    public void setMainPhoto(Uri mainPhotoUri){
        this.mainPhotoUri = mainPhotoUri;
    }

    @Override
    public String toString() {
        return "ReportEntity{" +
                "reportId=" + reportId +
                ", eventId=" + eventId +
                ", reportDescription='" + reportDescription + '\'' +
                ", reportDate=" + reportDate +
                ", mainPhotoUri=" + mainPhotoUri +
                '}';
    }
}
