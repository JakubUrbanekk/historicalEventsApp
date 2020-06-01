package com.example.inzynierka.Database.equipment.accessories;

import com.example.inzynierka.Database.equipment.IEquipment;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AccessoriesDao {
    @Insert
    long insert(AccessoriesEntity accessoriesEntity);

    @Query("DELETE FROM accessoriesentity")
    void deleteAll();

    @Query("SELECT * from accessoriesentity")
    List<IEquipment> getAll();

    @Delete
    void delete(AccessoriesEntity accessoriesEntity);

    @Query("SELECT * FROM accessoriesentity WHERE id = :position")
    IEquipment get(long position);

    @Update
    void update(AccessoriesEntity accessoriesEntity);
}
