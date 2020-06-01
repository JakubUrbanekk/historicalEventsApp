package com.example.inzynierka.informations.citiesInformations;

import android.graphics.Bitmap;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WikipediaEntity {
    private List<String> paragraphs;
    private CityPhotoEntity cityPhoto;
    private CityPhotoEntity mapPhoto;

    public Bitmap getCityPhotoBitmap(){
        return cityPhoto.getBitmap();
    }

    public Bitmap getMapPhotoBitmap(){
       return mapPhoto.getBitmap();
    }

    public String getCityPhotoDescription(){
        return cityPhoto.getDescription();
    }
    public String getMapPhotoDescription(){
        return mapPhoto.getDescription();
    }
}
