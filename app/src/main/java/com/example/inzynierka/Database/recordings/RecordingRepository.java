package com.example.inzynierka.Database.recordings;

import android.app.Application;

import com.example.inzynierka.Database.AppRoomDatabase;
import com.example.inzynierka.Database.videos.VideoDao;
import com.example.inzynierka.Database.videos.VideoEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RecordingRepository {
    private RecordingDao recordingDao;
    public RecordingRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        recordingDao = db.recordingDao();
    }
    public long insert(RecordingEntity videoEntity){
        return recordingDao.insert(videoEntity);
    }
    public void update(RecordingEntity videoEntity){
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recordingDao.update(videoEntity);
            }
        });

    }
    public LiveData<List<RecordingEntity>> getAllVideos() {
        return recordingDao.getAllRecording();
    }
    public LiveData<List<RecordingEntity>> getVideosFromReportById(Integer reportId) {
        return recordingDao.getRecordingFromReportById(reportId);
    }
    public void delete(RecordingEntity videoEntity){
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recordingDao.delete(videoEntity);
            }
        });

    }
}
