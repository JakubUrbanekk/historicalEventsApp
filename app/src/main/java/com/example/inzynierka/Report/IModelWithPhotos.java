package com.example.inzynierka.Report;

import com.example.inzynierka.Database.Photo.PhotoEntity;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public interface IModelWithPhotos {
    public void addPhotoToList(PhotoEntity photoEntity);
    public MutableLiveData<List<PhotoEntity>> getLiveDataPhotoList();
    public List<PhotoEntity> getPhotosList();
}
