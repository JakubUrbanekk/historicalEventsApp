package com.example.inzynierka.Photo;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Photo")
public class PhotoEntity {
    @DatabaseField(generatedId=true)
    int photoId;
    @DatabaseField
    String photoUri;
   // @DatabaseField(foreign = true)
    int reportId;

    public PhotoEntity() {
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public Uri getPhotoUri() {
        Uri uri = Uri.parse(photoUri);
        return uri;
    }

    public void setPhotoUri(Uri photoUri) {
        String uri = photoUri.toString();
        this.photoUri = uri;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}

