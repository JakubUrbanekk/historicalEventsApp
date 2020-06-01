package com.example.inzynierka.addons;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CityPicker{

    public static String getCityName(Activity currentActivity, double longitude, double latitude) {
        Geocoder geocoder = new Geocoder(currentActivity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        return cityName;
    }
}
