package com.example.inzynierka.Report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inzynierka.Database.DatabaseManager;
import com.example.inzynierka.Photo.PhotoEntity;
import com.example.inzynierka.Photo.PhotosAdapter;
import com.example.inzynierka.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ReportActivity extends OrmLiteBaseActivity<DatabaseManager> {
    Button addPhotos;
    ImageView mainPhoto;
    RecyclerView recyclerViewPhotosList;
    private static PhotosAdapter photosAdapter;
    private static final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initButtons();
    }

    private void initButtons() {
        mainPhoto = (ImageView) findViewById(R.id.imageViewReportMainPhoto);
        addPhotos = (Button) findViewById(R.id.buttonAddPhoto);
        recyclerViewPhotosList = (RecyclerView) findViewById(R.id.recyclerViewReportPhotosList);
    }

    public void addPhotos(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        } else {
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    if (data != null) {
                        if (data.getData() != null) {
                            Uri mImageUri = data.getData();
                            addPhotoToDatabase(mImageUri);

                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    addPhotoToDatabase(uri);
                                }
                            } else {
                                Toast.makeText(this, "Nie wybraleś zdjęcia", Toast.LENGTH_LONG).show();
                            }
                            List<Uri> photosUri = getPhotosFromDatabase();
                            photosAdapter = new PhotosAdapter(photosUri, ReportActivity.this, mainPhoto);
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerViewPhotosList.setLayoutManager(horizontalLayoutManager);
                            recyclerViewPhotosList.setAdapter(photosAdapter);
                            break;

                        }
                    }
            }
    }

    private void addPhotoToDatabase(Uri photoUri) {
        // get our dao
        try {
            Dao<PhotoEntity, Integer> photoDao = getHelper().getPhotoDao();
            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setPhotoUri(photoUri);
            photoDao.create(photoEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private List<Uri> getPhotosFromDatabase(){
        List<Uri> photosUri = new ArrayList<>();
        try {
            Dao<PhotoEntity, Integer> photoDao = getHelper().getPhotoDao();
            List<PhotoEntity> listPhotosEntities = photoDao.queryForAll();
            for (PhotoEntity photoEntity : listPhotosEntities){
                Uri photoUri = photoEntity.getPhotoUri();
                photosUri.add(photoUri);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photosUri;
    }
}

