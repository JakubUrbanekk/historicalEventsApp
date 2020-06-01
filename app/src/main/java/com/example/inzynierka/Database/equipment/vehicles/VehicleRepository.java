package com.example.inzynierka.Database.equipment.vehicles;

import android.app.Application;

import com.example.inzynierka.Database.AppRoomDatabase;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Database.equipment.IEquipmentRepository;

import java.util.List;

public class VehicleRepository implements IEquipmentRepository {
    private VehicleDao vehicleDao;

    public VehicleRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        vehicleDao = db.vehicleDao();
    }
    public long insert(IEquipment iEquipment){
        VehicleEntity vehicleEntity = new VehicleEntity(iEquipment);
        return vehicleDao.insert(vehicleEntity);
    }
    public List<IEquipment> getAll(){
        return vehicleDao.getAll();
    }

    @Override
    public void delete(IEquipment iEquipment) {
        VehicleEntity accessoriesEntity = new VehicleEntity(iEquipment, true);
        vehicleDao.delete(accessoriesEntity);
    }
    @Override
    public IEquipment get(long position) {
        return vehicleDao.get(position);
    }

    @Override
    public void update(IEquipment equipment) {
        VehicleEntity accessoriesEntity = new VehicleEntity(equipment, true);
         vehicleDao.update(accessoriesEntity);
    }
}
