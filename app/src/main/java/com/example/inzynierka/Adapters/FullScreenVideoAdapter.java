package com.example.inzynierka.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FullScreenVideoAdapter extends PagerAdapter {
    private Activity activity;
    private List<VideoEntity> videosList;
    private List<VideoView> videoViews;
    private LayoutInflater inflater;
    private final String TAG = "FullScreenAdapter";
    // constructor
    public FullScreenVideoAdapter(Activity activity) {
        this.activity = activity;
        videosList = new ArrayList<>();
        videoViews = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return this.videosList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        VideoEntity videoEntity = videosList.get(position);
        TextInputEditText vidDescription;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.item_video_with_edittext, container,
                false);

        VideoView vidDisplay = (VideoView) viewLayout.findViewById(R.id.itemVideo);
        videoViews.add(vidDisplay);
        vidDescription = (TextInputEditText) viewLayout.findViewById(R.id.itemVideoET);

        vidDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "change photo description " + s);
                videoEntity.setVideoDescription(s.toString());
            }
        });

        ViewGroup.LayoutParams params=vidDisplay.getLayoutParams();
        params.height=1400;
        params.width=1400;
        vidDisplay.setLayoutParams(params);
        setVideoToVideoView(videoEntity.getVideoUri(), vidDisplay);
        setExistingDescription(videoEntity.getVideoDescription(), vidDescription);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    private void setExistingDescription(String description, TextInputEditText etInput){
        if (description!=null){
            etInput.setText(description);
        }
    }

    private void setVideoToVideoView(Uri videoUri, VideoView vidDisplay){
        MediaController mediaController=new MediaController(activity);
        mediaController.setAnchorView(vidDisplay);
        vidDisplay.setVideoURI(videoUri);
        vidDisplay.setMediaController(mediaController);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }

    public void setVideos(List<VideoEntity> videosList){
        this.videosList = videosList;
        notifyDataSetChanged();
    }

    public List<VideoView> getVideoViews() {
        return videoViews;
    }
}
