package com.example.inzynierka.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.informations.EventDetails;
import com.example.inzynierka.Database.recordings.RecordingEntity;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Ignore;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder> {
    List<RecordingEntity> recordingEntities;

    public RecordingAdapter (){
        recordingEntities = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingAdapter.ViewHolder holder, int position) {
        RecordingEntity entity = recordingEntities.get(position);
        holder.imageView.setOnClickListener(view -> {
                    MediaPlayer mp=new MediaPlayer();
                    try{
                        log.info("Recording uri " + entity.getRecordUri());
                        mp.setDataSource(entity.getRecordUri());//path to your audio file here
                        mp.prepare();
                        mp.start();
                    }catch(Exception e){e.printStackTrace();}
            }
        );
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                entity.setRecordDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void setRecordings(List<RecordingEntity> recordings){
        recordingEntities.clear();
        recordingEntities.addAll(recordings);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return recordingEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText editText;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = (TextInputEditText) itemView.findViewById(R.id.itemRecordingET);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewRecording);
        }


    }
}
