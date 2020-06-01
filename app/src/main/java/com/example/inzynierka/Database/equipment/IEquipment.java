package com.example.inzynierka.Database.equipment;


import com.example.inzynierka.addons.FinalVariables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class IEquipment {
    @PrimaryKey(autoGenerate = true)
     protected long id;
     protected String name;
     protected String category;
     protected String description;
     protected String photoUri;

    public IEquipment(String name, String category, String photoUri, String description) {
        this.name = name;
        this.category = category;
        this.photoUri = photoUri;
        this.description = description;
    }
    public IEquipment(String name){
        this.name = name;
        this.category = FinalVariables.CLOTH_NOT_SELECTED_CONST;
        this.photoUri = "";
        this.description = "Brak";
    }
}
