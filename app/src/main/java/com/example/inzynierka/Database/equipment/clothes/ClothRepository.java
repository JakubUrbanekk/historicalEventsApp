package com.example.inzynierka.Database.equipment.clothes;

import android.app.Application;

import com.example.inzynierka.Database.AppRoomDatabase;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Database.equipment.IEquipmentRepository;

import java.util.List;

import lombok.extern.java.Log;

@Log
public class ClothRepository implements IEquipmentRepository {
    private ClothDao clothDao;

    public ClothRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        clothDao = db.clothDao();
    }
    public long insert(IEquipment iEquipment){
        ClothEntity clothEntity = new ClothEntity(iEquipment);
        return clothDao.insert(clothEntity);
    }
    public List<IEquipment> getAll(){
        return clothDao.getAll();
    }

    @Override
    public void delete(IEquipment IEquipment) {
        ClothEntity clothEntity = new ClothEntity(IEquipment, true);
        clothDao.delete(clothEntity);
    }
    @Override
    public IEquipment get(long position) {
        return clothDao.get(position);
    }

    @Override
    public void update(IEquipment equipment) {
        ClothEntity accessoriesEntity = new ClothEntity(equipment, true);
        clothDao.update(accessoriesEntity);
    }
}
