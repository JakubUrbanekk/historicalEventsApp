package com.example.inzynierka.Database;

import android.net.Uri;

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
        public static String toString(Uri uri){
            return uri.toString();
        }
    }

