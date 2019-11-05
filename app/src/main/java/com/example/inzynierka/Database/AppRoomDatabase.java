package com.example.inzynierka.Database;

import android.content.Context;

import com.example.inzynierka.Photo.PhotoDao;
import com.example.inzynierka.Photo.PhotoEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PhotoEntity.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract PhotoDao photoDao();

    private static volatile AppRoomDatabase INSTANCE;

    static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}