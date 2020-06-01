package com.example.inzynierka.Database.equipment.clothes;

import com.example.inzynierka.Database.equipment.IEquipment;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ClothDao {
    @Insert
    long insert(ClothEntity clothEntity);

    @Query("DELETE FROM clothentity")
    void deleteAll();

    @Query("SELECT * from clothentity")
    List<IEquipment> getAll();

    @Delete
    void delete(ClothEntity clothEntity);

    @Query("SELECT * FROM clothentity WHERE id = :position")
    IEquipment get(long position);

    @Update
    void update(ClothEntity accessoriesEntity);
}
