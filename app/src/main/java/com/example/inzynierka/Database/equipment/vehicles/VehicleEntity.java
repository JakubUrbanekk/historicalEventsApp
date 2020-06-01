package com.example.inzynierka.Database.equipment.vehicles;

import com.example.inzynierka.Database.equipment.IEquipment;

import androidx.room.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class VehicleEntity extends IEquipment {
    public VehicleEntity(IEquipment iEquipment){
        this.name = iEquipment.getName();
        this.category = iEquipment.getCategory();
        this.photoUri = iEquipment.getPhotoUri();
        this.description = iEquipment.getDescription();
    }
    public VehicleEntity(IEquipment iEquipment, boolean s){
        this.id = iEquipment.getId();
        this.name = iEquipment.getName();
        this.category = iEquipment.getCategory();
        this.photoUri = iEquipment.getPhotoUri();
        this.description = iEquipment.getDescription();
    }
}