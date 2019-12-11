package com.example.inzynierka.Database;

import android.content.Context;

import com.example.inzynierka.Database.Photo.PhotoDao;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportDao;
import com.example.inzynierka.Database.Report.ReportEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {PhotoEntity.class, ReportEntity.class}, version = 30, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public abstract PhotoDao photoDao();
    public abstract ReportDao reportDao();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}