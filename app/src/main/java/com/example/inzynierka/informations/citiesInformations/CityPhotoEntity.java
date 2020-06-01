package com.example.inzynierka.informations.citiesInformations;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CityPhotoEntity {
    private Bitmap bitmap;
    private String description;
}
