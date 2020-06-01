package com.example.inzynierka.Report;

import com.example.inzynierka.Database.videos.VideoEntity;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public interface IModelWithVideos {
    public void addVideoToList(VideoEntity videoList);
    public MutableLiveData<List<VideoEntity>> getLiveDataVideoList();
    public List<VideoEntity> getVideoList();
}
