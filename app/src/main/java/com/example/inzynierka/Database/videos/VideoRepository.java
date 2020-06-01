package com.example.inzynierka.Database.videos;

import android.app.Application;

import com.example.inzynierka.Database.AppRoomDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class VideoRepository {
    private VideoDao videoDao;
    public VideoRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        videoDao = db.videoDao();
    }
    public long insert(VideoEntity videoEntity){
        return videoDao.insert(videoEntity);
    }
    public void update(VideoEntity videoEntity){
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                videoDao.update(videoEntity);
            }
        });

    }
    public LiveData<List<VideoEntity>> getAllVideos() {
        return videoDao.getAllVideos();
    }
    public LiveData<List<VideoEntity>> getVideosFromReportById(Integer reportId) {
        return videoDao.getPhotosFromReportById(reportId);
    }
    public void delete(VideoEntity videoEntity){
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                videoDao.delete(videoEntity);
            }
        });

    }
}
