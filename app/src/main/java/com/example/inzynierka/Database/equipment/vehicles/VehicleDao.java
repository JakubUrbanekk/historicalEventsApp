package com.example.inzynierka.Database.equipment.vehicles;

import com.example.inzynierka.Database.equipment.IEquipment;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface VehicleDao {
    @Insert
    long insert(VehicleEntity vehicleEntity);

    @Query("DELETE FROM vehicleentity")
    void deleteAll();

    @Query("SELECT * from vehicleentity")
    List<IEquipment> getAll();

    @Delete
    void delete(VehicleEntity accessoriesEntity);

    @Query("SELECT * FROM vehicleentity WHERE id = :position")
    IEquipment get(long position);

    @Update
    void update(VehicleEntity accessoriesEntity);
}
