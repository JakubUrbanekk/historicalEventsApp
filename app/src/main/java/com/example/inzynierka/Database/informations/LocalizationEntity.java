package com.example.inzynierka.Database.informations;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "localization")
public class LocalizationEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private double longitude;
    private double latitude;

    public LocalizationEntity(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
