package com.example.inzynierka.Report;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable (tableName = "Report")
public class ReportEntity {
    @DatabaseField(generatedId=true)
    int reportId;
    @DatabaseField(foreign = true, columnName = "eventId")
    int eventId;
    @ForeignCollectionField
    List<PhotoEntity> reportListPhotos;

    public ReportEntity() {
    }

    public ReportEntity(int reportId, int eventId, List<PhotoEntity> reportListPhotos) {
        this.reportId = reportId;
        this.eventId = eventId;
        this.reportListPhotos = reportListPhotos;
    }
}
