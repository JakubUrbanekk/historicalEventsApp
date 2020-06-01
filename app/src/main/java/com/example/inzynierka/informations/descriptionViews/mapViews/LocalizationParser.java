package com.example.inzynierka.informations.descriptionViews.mapViews;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.inzynierka.informations.listOfInformations.Scrapper;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@AllArgsConstructor
@NoArgsConstructor
@Log
public class LocalizationParser {
    Context context;

    public LatLng getCoordsByCityName(String city){
        if (city.equals(Scrapper.NO_DATA)){
            return new LatLng(0,0);
        }
        Geocoder geocoder = new Geocoder(context);
        city = city.concat(", Poland");
        log.info("GETTING CORDS FOR " + city);
        LatLng coords;
        try {
            List<Address> cityAddress = geocoder.getFromLocationName(city,1);
            if(cityAddress.size() == 0 ){
                return new LatLng(0,0);
            }

            Address address = cityAddress.get(0);
            log.info("Address for localization " + address);
            coords = new LatLng(address.getLatitude(), address.getLongitude());


        } catch (IOException e) {
            coords = new LatLng(0,0);
            log.info("COULDN'T GET COORDS");
        }
        return coords;
    }
}
