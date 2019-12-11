package com.example.inzynierka.Database.Photo;

import android.net.Uri;

import com.example.inzynierka.Database.Report.ReportEntity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "photo", foreignKeys = @ForeignKey(entity = ReportEntity.class,
        parentColumns = "reportId",
        childColumns = "reportIdForPhoto",
        onDelete = CASCADE), indices = {@Index(value = {"photoId", "reportIdForPhoto"})})
public class PhotoEntity {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    Integer photoId;
    @ColumnInfo(name = "uri")
    String photoUri;
    String photoDescription;
    Integer reportIdForPhoto;

    public String getPhotoDescription() {
        return photoDescription;
    }
    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }
    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
    public PhotoEntity(Uri uri) {
        photoUri = uri.toString();
    }
    public PhotoEntity(){
    }
    @NonNull
    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(@NonNull Integer photoId) {
        this.photoId = photoId;
    }

    public Integer getReportId() {
        return reportIdForPhoto;
    }

    public void setReportId(Integer reportId) {
        this.reportIdForPhoto = reportId;
    }

    public void setPhotoUri(Uri photoUri) {
        String uri = photoUri.toString();
        this.photoUri = uri;
    }

    public Uri getPhotoUri() {
        return photoUri == null ? null : Uri.parse(photoUri);
    }

    @Override
    public String toString() {
        return "PhotoEntity{" +
                "photoId=" + photoId +
                ", photoUri='" + photoUri + '\'' +
                ", reportId=" + reportIdForPhoto +
                ", opis " + photoDescription
                +'}';
    }
}

