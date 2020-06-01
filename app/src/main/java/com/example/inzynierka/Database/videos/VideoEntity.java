package com.example.inzynierka.Database.videos;

import android.net.Uri;

import com.example.inzynierka.Database.Report.ReportEntity;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "video", foreignKeys = @ForeignKey(entity = ReportEntity.class,
        parentColumns = "reportId",
        childColumns = "reportIdForVideo",
        onDelete = CASCADE))
public class VideoEntity implements Serializable{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer videoId;
    @ColumnInfo(name = "uri")
    String videoUri;
    String videoDescription;
    Integer reportIdForVideo;

    public VideoEntity(Uri videoUri) {
        this.videoUri = videoUri.toString();
    }
    public VideoEntity(){

    }

    @NonNull
    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(@NonNull Integer videoId) {
        this.videoId = videoId;
    }

    public Uri getVideoUri() {
        return videoUri == null ? null : Uri.parse(videoUri);
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public Integer getReportIdForVideo() {
        return reportIdForVideo;
    }

    public void setReportIdForVideo(Integer reportIdForVideo) {
        this.reportIdForVideo = reportIdForVideo;
    }
    public void setVideoUri(Uri videoUri) {
        String uri = videoUri.toString();
        this.videoUri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoEntity that = (VideoEntity) o;
        return videoId.equals(that.videoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId);
    }

}
