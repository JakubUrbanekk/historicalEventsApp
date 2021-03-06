package com.example.inzynierka.Database;

import android.net.Uri;

import com.example.inzynierka.CustomDate.CustomData;
import com.example.inzynierka.Database.Photo.PhotoEntity;

import java.util.Date;

import androidx.room.TypeConverter;

    public class Converters {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
        @TypeConverter
        public static Uri fromString(String stringValue) {
            return stringValue == null ? null : Uri.parse(stringValue);
        }

        @TypeConverter
        public static String customDateToString(CustomData customData){
            return customData.toString();
        }
        @TypeConverter
        public static CustomData customDateFromString(String cutomDate){
            return new CustomData(cutomDate);
        }
        @TypeConverter
        public static String toString(Uri uri){
            return uri.toString();
        }
        @TypeConverter
        public static PhotoEntity toPhotoEntity(Uri uri){
            return new PhotoEntity(uri);
        }
        @TypeConverter
        public static String photoToUri(PhotoEntity photoEntity) {
            if (photoEntity == null) {
                return "";
            } else {
                return photoEntity.getPhotoUri().toString();
            }
        }
    }

