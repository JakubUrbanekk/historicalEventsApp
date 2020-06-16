package com.example.inzynierka.Database;

import android.content.Context;

import com.example.inzynierka.Database.Photo.PhotoDao;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportDao;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.equipment.accessories.AccessoriesDao;
import com.example.inzynierka.Database.equipment.accessories.AccessoriesEntity;
import com.example.inzynierka.Database.equipment.clothes.ClothDao;
import com.example.inzynierka.Database.equipment.clothes.ClothEntity;
import com.example.inzynierka.Database.equipment.vehicles.VehicleDao;
import com.example.inzynierka.Database.equipment.vehicles.VehicleEntity;
import com.example.inzynierka.Database.equipment.weapons.WeaponDao;
import com.example.inzynierka.Database.equipment.weapons.WeaponEntity;
import com.example.inzynierka.Database.informations.EventDetails;
import com.example.inzynierka.Database.informations.EventDetailsDao;
import com.example.inzynierka.Database.informations.LocalizationDao;
import com.example.inzynierka.Database.informations.LocalizationEntity;
import com.example.inzynierka.Database.recordings.RecordingDao;
import com.example.inzynierka.Database.recordings.RecordingEntity;
import com.example.inzynierka.Database.videos.VideoDao;
import com.example.inzynierka.Database.videos.VideoEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {PhotoEntity.class, ReportEntity.class, VideoEntity.class, LocalizationEntity.class,
        EventDetails.class, ClothEntity.class, WeaponEntity.class, RecordingEntity.class, AccessoriesEntity.class, VehicleEntity.class},
        version = 46, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public abstract PhotoDao photoDao();
    public abstract VideoDao videoDao();
    public abstract ReportDao reportDao();
    public abstract ClothDao clothDao();
    public abstract WeaponDao weaponDao();
    public abstract AccessoriesDao accessoriesDao();
    public abstract RecordingDao recordingDao();
    public abstract VehicleDao vehicleDao();
    public abstract EventDetailsDao eventDetailsDao();
    public abstract LocalizationDao localizationDao();

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