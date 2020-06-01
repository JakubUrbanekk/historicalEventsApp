package com.example.inzynierka.Database.equipment.clothes;

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
public class ClothEntity extends IEquipment {
    public ClothEntity (IEquipment iEquipment){
        this.name = iEquipment.getName();
        this.category = iEquipment.getCategory();
        this.photoUri = iEquipment.getPhotoUri();
        this.description = iEquipment.getDescription();
    }

    public ClothEntity(IEquipment iEquipment, boolean b) {
        this.id = iEquipment.getId();
        this.name = iEquipment.getName();
        this.category = iEquipment.getCategory();
        this.photoUri = iEquipment.getPhotoUri();
        this.description = iEquipment.getDescription();
    }
}
