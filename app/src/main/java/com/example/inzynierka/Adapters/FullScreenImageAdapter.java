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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private List<PhotoEntity> photosList;
    private LayoutInflater inflater;
    private final String TAG = "FullScreenAdapter";

    // constructor
    public FullScreenImageAdapter(Activity activity) {
        this.activity = activity;
        photosList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return this.photosList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoEntity photoEntity = photosList.get(position);
        ImageView imgDisplay;
        TextInputEditText imgDescription;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.item_photo_with_edittext, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.itemPhotoIV);
        imgDescription = (TextInputEditText) viewLayout.findViewById(R.id.itemPhotoET);

        imgDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "change photo description " + s);
                photoEntity.setPhotoDescription(s.toString());
            }
        });

        setPhotoToImageView(photoEntity.getPhotoUri(), imgDisplay);
        setExistingDescription(photoEntity.getPhotoDescription(), imgDescription);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    private void setExistingDescription(String description, TextInputEditText etInput){
        if (description!=null){
            etInput.setText(description);
        }
    }

    private void setPhotoToImageView(Uri photoUri, ImageView imgDisplay){
        Glide
                .with(activity.getApplicationContext())
                .load(photoUri)
                .placeholder(R.drawable.noimge)
                .centerCrop()
                .into(imgDisplay);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

    public void setPhotos(List<PhotoEntity> photoEntities){
        photosList = photoEntities;
        notifyDataSetChanged();
    }
}