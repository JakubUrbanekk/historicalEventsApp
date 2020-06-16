package com.example.inzynierka.Database.recordings;

import com.example.inzynierka.Database.videos.VideoEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RecordingDao {
    @Insert
    long insert(RecordingEntity videoEntity);
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(RecordingEntity videoEntity);
    @Delete
    void delete(RecordingEntity videoEntity);
    @Query("SELECT * FROM recording WHERE reportIdForRecording = :id")
    LiveData<List<RecordingEntity>> getRecordingFromReportById(Integer id);
    @Query("SELECT * from recording")
    LiveData<List<RecordingEntity>> getAllRecording();
}
