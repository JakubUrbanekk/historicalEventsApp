package com.example.inzynierka.Report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.recordings.RecordingEntity;
import com.example.inzynierka.Database.videos.VideoEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import lombok.extern.java.Log;

@Log
public abstract class OpenPhotoGalleryActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST_CODE = 1;
    public static final int GALLERY_VIDEO_REQUEST_CODE = 4;
    public static final int CAMERA_REQUEST_CODE = 2;
    public static final int VIDEO_REQUEST_CODE = 3;
    public static final int DICAPHONE_REQUEST_CODE = 5;
    public ModelWithPhotos viewModel;
    public ModelWithVideos videoModel;
    public ModelWithRecording recordingModel;
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

        recordingModel = new ModelWithRecording();
        recordingModel.getLiveDataRecordingList().observe(this, this::onRecordingAdded);
        }

    protected abstract void onRecordingAdded(List<RecordingEntity> l);

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
        log.info("Open dicaphone method starting");
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, requestCode);
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
                case DICAPHONE_REQUEST_CODE:
                    log.info("Return from dicaphone");
                        if (resultCode == RESULT_OK) {
                            Uri audioUri = data.getData();
                            String path = getPathForAudio(this, audioUri);
                            Uri uriPath = Uri.fromFile(new File(path));
                            log.info("Uri " + uriPath);
                            if (audioUri != null) {
                                RecordingEntity recordingEntity = new RecordingEntity(uriPath.toString());
                                recordingModel.addRecordingToList(recordingEntity);
                            }
                            // make use of this MediaStore uri
                            // e.g. store it somewhere
                        }
                        else {
                            // react meaningful to problems
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

    private String getPathForAudio(Context context, Uri uri)
    {
        String result = null;
        Cursor cursor = null;

        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor == null) {
                result = uri.getPath();
            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                result = cursor.getString(column_index);
                cursor.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}
