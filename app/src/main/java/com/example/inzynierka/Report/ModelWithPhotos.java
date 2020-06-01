package com.example.inzynierka.Report;

import com.example.inzynierka.Database.Photo.PhotoEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public  class ModelWithPhotos implements IModelWithPhotos{

    MutableLiveData<List <PhotoEntity>> photoEntities = new MutableLiveData<>();

    @Override
    public void addPhotoToList(PhotoEntity photoEntity) {
        List<PhotoEntity> photos = photoEntities.getValue();
        if (photos == null){
            photos = new ArrayList<>();
            photos.add(photoEntity);
        }
        else {
            photos.add(photoEntity);
        }
        photoEntities.setValue(photos);
    }

    @Override
    public MutableLiveData<List<PhotoEntity>> getLiveDataPhotoList() {
        return photoEntities;
    }

    @Override
    public List<PhotoEntity> getPhotosList() {
        if (photoEntities.getValue() == null){
            return new ArrayList<>();
        }
        return photoEntities.getValue();
    }

    public void setPhotoEntities(List<PhotoEntity> photoEntities) {
        this.photoEntities.setValue(photoEntities);
    }
}
