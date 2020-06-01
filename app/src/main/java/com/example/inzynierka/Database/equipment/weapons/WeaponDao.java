package com.example.inzynierka.Database.equipment.weapons;

import com.example.inzynierka.Database.equipment.IEquipment;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WeaponDao {
    @Insert
    long insert(WeaponEntity weaponEntity);

    @Query("DELETE FROM weaponentity")
    void deleteAll();

    @Query("SELECT * from weaponentity")
    List<IEquipment> getAll();

    @Delete
    void delete(WeaponEntity accessoriesEntity);

    @Query("SELECT * FROM accessoriesentity WHERE id = :position")
    IEquipment get(long position);

    @Update
    void update(WeaponEntity accessoriesEntity);
}
