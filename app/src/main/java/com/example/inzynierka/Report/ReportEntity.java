package com.example.inzynierka.Report;

import com.example.inzynierka.Photo.PhotoEntity;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "Report")
public class ReportEntity {
    @DatabaseField(generatedId=true)
    int reportId;
   // @DatabaseField(foreign = true, columnName = "eventId")
    int eventId;
    @ForeignCollectionField
    ForeignCollection<PhotoEntity> reportListPhotos;
    @DatabaseField
    String reportDescription;
    @DatabaseField
    boolean creationCompleted;

    public ReportEntity() {
        creationCompleted = false;
    }


    public ReportEntity(int reportId, int eventId, ForeignCollection<PhotoEntity> reportListPhotos, String reportDescription) {
        this.reportId = reportId;
        this.eventId = eventId;
        this.reportListPhotos = reportListPhotos;
        this.reportDescription = reportDescription;
        creationCompleted = false;

    }

    public boolean isCreationCompleted() {
        return creationCompleted;
    }

    public void setCreationCompleted(boolean creationCompleted) {
        this.creationCompleted = creationCompleted;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public ForeignCollection<PhotoEntity> getReportListPhotos() {
        return reportListPhotos;
    }

    public void setReportListPhotos(ForeignCollection<PhotoEntity> reportListPhotos) {
        this.reportListPhotos = reportListPhotos;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    @Override
    public String toString() {
        return "ReportEntity{" +
                "reportId=" + reportId +
                ", eventId=" + eventId +
                ", reportListPhotos=" + reportListPhotos +
                ", reportDescription='" + reportDescription + '\'' +
                ", creationCompleted=" + creationCompleted +
                '}';
    }
}
