package com.example.inzynierka.Database.Report;

import com.example.inzynierka.Database.Photo.PhotoEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReportDao {
    @Insert
    long insert(ReportEntity reportEntity);
    @Query("SELECT * from reports ORDER BY reportDate ASC")
    LiveData<List<ReportEntity>> getAllReports();
    @Query("SELECT * FROM reports WHERE reportId = :id")
    LiveData<ReportEntity> getReportById(Integer id);
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(ReportEntity reportEntity);
    @Delete
    void delete(ReportEntity reportEntity);
    @Query("SELECT * from reports ORDER BY reportTitle ASC")
    LiveData<List<ReportEntity>> getReportsByTitleOrder();
}
