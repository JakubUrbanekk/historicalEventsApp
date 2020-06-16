package com.example.inzynierka.Report;

import com.example.inzynierka.Database.recordings.RecordingEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class ModelWithRecording {
    MutableLiveData<List <RecordingEntity>> recordingEntities = new MutableLiveData<>();

    public void addRecordingToList(RecordingEntity videoEntity) {
        List<RecordingEntity> videos = recordingEntities.getValue();
        if (videos == null){
            videos = new ArrayList<>();
            videos.add(videoEntity);
        }
        else {
            videos.add(videoEntity);
        }
        recordingEntities.setValue(videos);
    }

    public MutableLiveData<List<RecordingEntity>> getLiveDataRecordingList() {
        return recordingEntities;
    }

    public List<RecordingEntity> getRecordingList() {
        if (recordingEntities.getValue() == null){
            return new ArrayList<>();
        }
        return recordingEntities.getValue();
    }

    public void setRecordingEntities(List<RecordingEntity> recordingEntities) {
        this.recordingEntities.setValue(recordingEntities);
    }
}
