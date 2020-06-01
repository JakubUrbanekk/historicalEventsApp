package com.example.inzynierka.Report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.videos.VideoEntity;

import java.io.ByteArrayOutputStream;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

public abstract class OpenPhotoGalleryActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST_CODE = 1;
    public static final int GALLERY_VIDEO_REQUEST_CODE = 4;
    public static final int CAMERA_REQUEST_CODE = 2;
    public static final int VIDEO_REQUEST_CODE = 3;
    public static final int DICAPHONE_REQUEST_CODE = 4;
    public ModelWithPhotos viewModel;
    public ModelWithVideos videoModel;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        viewModel = new ModelWithPhotos();
        viewModel.getLiveDataPhotoList().observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(List<PhotoEntity> photoEntities) {
                onPhotosAdded(photoEntities);
            }
        });
        videoModel = new ModelWithVideos();
        videoModel.getLiveDataVideoList().observe(this, new Observer<List<VideoEntity>>() {
            @Override
            public void onChanged(List<VideoEntity> videoEntities) {
                onVideosAdded(videoEntities);
            }
        });
    }

    protected abstract void onPhotosAdded(List <PhotoEntity> photoEntities);

    protected abstract void onVideosAdded(List <VideoEntity> videoEntities);

    public void openGallery(View view, int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        if(requestCode == GALLERY_REQUEST_CODE) {
            photoPickerIntent.setType("image/*");
        } else if (requestCode == GALLERY_VIDEO_REQUEST_CODE) {
            photoPickerIntent.setType("video/*");
        }
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        } else {
            startActivityForResult(photoPickerIntent, requestCode);
        }
    }

    public void openCamera(View view, int requestCode) {
        Intent cameraIntent;
        if(requestCode == CAMERA_REQUEST_CODE){
            cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        }
        else {
            cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }
        startActivityForResult(cameraIntent, requestCode);
    }
    public void openDicaphone(int requestCode){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, DICAPHONE_REQUEST_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            PhotoEntity photoEntity = new PhotoEntity(uri);
                            viewModel.addPhotoToList(photoEntity);
                        }
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    if (image !=null) {
                        Uri imageUri = getImageUri(this, image);
                        PhotoEntity photoEntity = new PhotoEntity(imageUri);
                        viewModel.addPhotoToList(photoEntity);
                    }
                    break;
                case GALLERY_VIDEO_REQUEST_CODE:
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            VideoEntity videoEntity = new VideoEntity(uri);
                            videoModel.addVideoToList(videoEntity);
                        }
                    }
                    break;
                case VIDEO_REQUEST_CODE:
                    Uri video = data.getData();
                    if (video !=null) {
                        VideoEntity videoEntity = new VideoEntity(video);
                        videoModel.addVideoToList(videoEntity);
                    }
                    break;
                default :
                    break;
            }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
