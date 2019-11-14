package com.example.inzynierka.Database.Report;

import com.example.inzynierka.Database.Photo.PhotoEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ReportDao {
    @Insert
    void insert(ReportEntity reportEntity);
    @Query("SELECT * from reports ORDER BY reportDate ASC")
    LiveData<List<ReportEntity>> getAllReports();
}
