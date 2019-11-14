package com.example.inzynierka.Database.Photo;

import android.app.Application;
import android.os.AsyncTask;

import com.example.inzynierka.Database.AppRoomDatabase;

public class PhotoRepository {
    private PhotoDao photoDao;
    public PhotoRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        photoDao = db.photoDao();
    }
        public void insert(PhotoEntity photoEntity){
            new insertAsyncTask(photoDao).execute(photoEntity);
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


