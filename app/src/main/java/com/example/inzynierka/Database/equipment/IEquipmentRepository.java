package com.example.inzynierka.Database.equipment;

import java.util.List;

public interface IEquipmentRepository {
    public long insert(IEquipment iEquipment);
    public List<IEquipment> getAll();
    public void delete(IEquipment iEquipment);
    public IEquipment get(long position);
    public void update(IEquipment equipment);
}
