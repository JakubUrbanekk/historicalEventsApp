package com.example.inzynierka.Database.equipment.weapons;

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
public class WeaponEntity extends IEquipment {
    public WeaponEntity (IEquipment iEquipment){
        this.name = iEquipment.getName();
        this.category = iEquipment.getCategory();
        this.photoUri = iEquipment.getPhotoUri();
        this.description = iEquipment.getDescription();
    }
    public WeaponEntity (IEquipment iEquipment, boolean s){
        this.id = iEquipment.getId();
        this.name = iEquipment.getName();
        this.category = iEquipment.getCategory();
        this.photoUri = iEquipment.getPhotoUri();
        this.description = iEquipment.getDescription();
    }
}