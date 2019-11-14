package com.example.inzynierka.Database.Photo;

import android.net.Uri;

import com.example.inzynierka.Database.Report.ReportEntity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "photo", foreignKeys = @ForeignKey(entity = ReportEntity.class,
        parentColumns = "reportId",
        childColumns = "reportId",
        onDelete = CASCADE))
public class PhotoEntity {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    Integer photoId;
    @ColumnInfo(name = "uri")
    String photoUri;
    Integer reportId;

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public PhotoEntity() {
    }

    @NonNull
    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(@NonNull Integer photoId) {
        this.photoId = photoId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public void setPhotoUri(Uri photoUri) {
        String uri = photoUri.toString();
        this.photoUri = uri;
    }

    @Override
    public String toString() {
        return "PhotoEntity{" +
                "photoId=" + photoId +
                ", photoUri='" + photoUri + '\'' +
                ", reportId=" + reportId +
                '}';
    }
}

