package com.example.inzynierka.Database.informations;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LocalizationDao {
    @Insert
    public long insert(LocalizationEntity localizationEntity);
    @Query("SELECT * from localization")
    List<LocalizationEntity> getAll();
    @Query("DELETE FROM localization")
    void deleteAll();
}
