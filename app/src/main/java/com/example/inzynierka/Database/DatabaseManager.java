package com.example.inzynierka.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.inzynierka.R;
import com.example.inzynierka.Photo.PhotoEntity;
import com.example.inzynierka.Report.ReportEntity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "historicalEvents";
    private static final String TAG = DatabaseManager.class.getName();
    private static final int DATABASE_VERSION = 5;
    private Dao<ReportEntity, Integer> reportDao = null;
    private Dao<PhotoEntity, Integer> photoDao = null;

    public DatabaseManager(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, Dao<ReportEntity, Integer> reportDao) {
        super(context, databaseName, factory, databaseVersion);
        this.reportDao = reportDao;
    }

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate");
            TableUtils.createTable(connectionSource, ReportEntity.class);
            TableUtils.createTable(connectionSource, PhotoEntity.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            Log.i(TAG, "on upgrade");
            TableUtils.dropTable(connectionSource, ReportEntity.class, true);
            TableUtils.dropTable(connectionSource, PhotoEntity.class, true);
            onCreate(database,connectionSource);
        }
        catch (SQLException e) {
            Log.e(TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }
    public Dao<ReportEntity, Integer> getReportDao() throws SQLException {
        if (reportDao == null) {
            reportDao = getDao(ReportEntity.class);
        }
        return reportDao;
    }
    public Dao<PhotoEntity, Integer> getPhotoDao() throws SQLException {
        if (photoDao == null) {
            photoDao = getDao(PhotoEntity.class);
        }
        return photoDao;
    }

    @Override
    public void close() {
        super.close();
        reportDao = null;

    }
}
