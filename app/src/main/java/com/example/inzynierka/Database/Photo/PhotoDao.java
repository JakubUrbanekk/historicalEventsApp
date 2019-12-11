package com.example.inzynierka.Database.Photo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PhotoDao {
    @Insert
    long insert(PhotoEntity photoEntity);
    @Query("SELECT * from photo ORDER BY photoId ASC")
    LiveData<List<PhotoEntity>> getAllPhotos();
    @Query("SELECT * FROM photo WHERE photoId = :id")
    PhotoEntity getPhotoById(Integer id);
    @Query("SELECT * FROM photo WHERE uri = :uri AND reportIdForPhoto = :reportId LIMIT 1")
    LiveData<PhotoEntity> getPhotoByUri(String uri, Integer reportId);
    @Query("SELECT * FROM photo WHERE reportIdForPhoto = :id")
    LiveData<List<PhotoEntity>> getPhotosFromReportById(Integer id);
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(PhotoEntity photoEntity);
    @Delete
    void delete(PhotoEntity photoEntity);
}
