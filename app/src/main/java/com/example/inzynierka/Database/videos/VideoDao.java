package com.example.inzynierka.Database.videos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface VideoDao {
    @Insert
    long insert(VideoEntity videoEntity);
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(VideoEntity videoEntity);
    @Delete
    void delete(VideoEntity videoEntity);
    @Query("SELECT * FROM video WHERE reportIdForVideo = :id")
    LiveData<List<VideoEntity>> getPhotosFromReportById(Integer id);
    @Query("SELECT * from video ORDER BY videoId ASC")
    LiveData<List<VideoEntity>> getAllVideos();
}
