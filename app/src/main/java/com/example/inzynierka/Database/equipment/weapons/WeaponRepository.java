package com.example.inzynierka.Database.equipment.weapons;

import android.app.Application;

import com.example.inzynierka.Database.AppRoomDatabase;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Database.equipment.IEquipmentRepository;

import java.util.List;

public class WeaponRepository implements IEquipmentRepository {
    private WeaponDao weaponDao;

    public WeaponRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        weaponDao = db.weaponDao();
    }
    public long insert(IEquipment iEquipment){
        WeaponEntity weaponEntity = new WeaponEntity(iEquipment);
        return weaponDao.insert(weaponEntity);
    }
    public List<IEquipment> getAll(){
        return weaponDao.getAll();
    }

    @Override
    public void delete(IEquipment iEquipment) {
        WeaponEntity accessoriesEntity = new WeaponEntity(iEquipment, true);
        weaponDao.delete(accessoriesEntity);
    }

    @Override
    public IEquipment get(long position) {
        return weaponDao.get(position);
    }

    @Override
    public void update(IEquipment equipment) {
        WeaponEntity accessoriesEntity = new WeaponEntity(equipment, true);
         weaponDao.update(accessoriesEntity);
    }
}
