package com.example.inzynierka.Database.Photo;

import android.app.Application;
import android.os.AsyncTask;

import com.example.inzynierka.Database.AppRoomDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class PhotoRepository {
    private PhotoDao photoDao;
    public PhotoRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        photoDao = db.photoDao();
    }
        public long insert(PhotoEntity photoEntity){
            return photoDao.insert(photoEntity);
        }
        public void update(PhotoEntity photoEntity){
            AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    photoDao.update(photoEntity);
                }
            });

        }

        public LiveData<List<PhotoEntity>> getAllPhotos() {
                return photoDao.getAllPhotos();
        }

        public void delete(PhotoEntity photoEntity){
        AppRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                photoDao.delete(photoEntity);
            }
        });

        }

        public PhotoEntity getPhotoById(Integer id){
            return photoDao.getPhotoById(id);
        }

        public LiveData<List<PhotoEntity>> getPhotosFromReportById(Integer id){
            return  photoDao.getPhotosFromReportById(id);
        }

        public LiveData<PhotoEntity> getPhotoByUri(String uri, Integer id){
            return photoDao.getPhotoByUri(uri, id);
        }

    private static class insertAsyncTask extends AsyncTask<PhotoEntity, Void, Void> {

    private PhotoDao asyncPhotoDao;

    insertAsyncTask(PhotoDao dao) {
        asyncPhotoDao = dao;
    }

    @Override
    protected Void doInBackground(final PhotoEntity... params) {
        asyncPhotoDao.insert(params[0]);
        return null;
    }
    }

}


