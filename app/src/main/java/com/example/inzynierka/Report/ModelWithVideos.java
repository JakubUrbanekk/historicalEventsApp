package com.example.inzynierka.Report;

import com.example.inzynierka.Database.videos.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class ModelWithVideos implements IModelWithVideos {

    MutableLiveData<List <VideoEntity>> videoEntities = new MutableLiveData<>();

    @Override
    public void addVideoToList(VideoEntity videoEntity) {
        List<VideoEntity> videos = videoEntities.getValue();
        if (videos == null){
            videos = new ArrayList<>();
            videos.add(videoEntity);
        }
        else {
            videos.add(videoEntity);
        }
        videoEntities.setValue(videos);
    }

    @Override
    public MutableLiveData<List<VideoEntity>> getLiveDataVideoList() {
        return videoEntities;
    }

    @Override
    public List<VideoEntity> getVideoList() {
        if (videoEntities.getValue() == null){
            return new ArrayList<>();
        }
        return videoEntities.getValue();
    }

    public void setVideoEntities(List<VideoEntity> videoEntities) {
        this.videoEntities.setValue(videoEntities);
    }
}
