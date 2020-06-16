package com.example.inzynierka.Database.recordings;

import com.example.inzynierka.Database.Report.ReportEntity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "recording", foreignKeys = @ForeignKey(entity = ReportEntity.class,
        parentColumns = "reportId",
        childColumns = "reportIdForRecording",
        onDelete = CASCADE))
@Getter
@Setter
public class RecordingEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer recordId;
    @ColumnInfo(name = "uri")
    String recordUri;
    String recordDescription;
    Integer reportIdForRecording;

    public RecordingEntity (String recordUri){
        this.recordUri = recordUri;
    }
}