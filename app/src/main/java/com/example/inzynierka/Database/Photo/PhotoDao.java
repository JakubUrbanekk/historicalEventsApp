package com.example.inzynierka.Database.Photo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PhotoDao {
    @Insert
    void insert(PhotoEntity photoEntity);
    @Query("SELECT * from photo ORDER BY photoId ASC")
    LiveData<List<PhotoEntity>> getAllPhotos();
}
