package com.example.inzynierka.Database.equipment.accessories;

import android.app.Application;

import com.example.inzynierka.Database.AppRoomDatabase;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Database.equipment.IEquipmentRepository;

import java.util.List;

public class AccessoriesRepository implements IEquipmentRepository {
    private AccessoriesDao accessoriesDao;

    public AccessoriesRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        accessoriesDao = db.accessoriesDao();
    }
    public long insert(IEquipment iEquipment){
        AccessoriesEntity accessoriesEntity = new AccessoriesEntity(iEquipment);
        return accessoriesDao.insert(accessoriesEntity);
    }
    public List<IEquipment> getAll(){
        return accessoriesDao.getAll();
    }

    @Override
    public void delete(IEquipment equipment) {
        AccessoriesEntity accessoriesEntity = new AccessoriesEntity(equipment, true);
        accessoriesDao.delete(accessoriesEntity);
    }

    @Override
    public IEquipment get(long position) {
        return accessoriesDao.get(position);
    }

    @Override
    public void update(IEquipment equipment) {
        AccessoriesEntity accessoriesEntity = new AccessoriesEntity(equipment, true);
         accessoriesDao.update(accessoriesEntity);
    }
}
