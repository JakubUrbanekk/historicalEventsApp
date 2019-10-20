package com.example.inzynierka.Report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inzynierka.Database.DatabaseManager;
import com.example.inzynierka.MainActivity;
import com.example.inzynierka.Photo.PhotoEntity;
import com.example.inzynierka.Photo.PhotosAdapter;
import com.example.inzynierka.R;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ReportActivity extends OrmLiteBaseActivity<DatabaseManager> {
    final static String TAG = ReportActivity.class.getName();
    Button addPhotos;
    ImageView mainPhoto;
    RecyclerView recyclerViewPhotosList;
    EditText editTextReportDescription;
    ReportEntity reportEntity;
    private static PhotosAdapter photosAdapter;
    private static final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        reportEntity = new ReportEntity();
        Dao<ReportEntity, Integer> reportDao = null;
        try {
            reportDao = getHelper().getReportDao();
            reportDao.create(reportEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initButtons();
    }

    private void initButtons() {
        editTextReportDescription = (EditText) findViewById(R.id.editTextReportDescription);
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
                                addPhotoToDatabase(mClipData);
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

    private void addPhotoToDatabase(ClipData mClipData) {
            for (int i = 0; i < mClipData.getItemCount(); i++) {
                ClipData.Item item = mClipData.getItemAt(i);
                Uri uri = item.getUri();
                addPhotoToDatabase(uri);
            }

    }

    private void addPhotoToDatabase(Uri photoUri) {
        // get our dao
        try {
            Log.e(TAG  , "before connecting" + reportEntity.toString());
            Dao<PhotoEntity, Integer> photoDao = getHelper().getPhotoDao();
            Dao<ReportEntity, Integer> reportDao = getHelper().getReportDao();
            reportEntity = reportDao.queryForId(reportEntity.getReportId());
            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setPhotoUri(photoUri);
            photoEntity.setReport(reportEntity);
            photoDao.create(photoEntity);
            Log.e(TAG, "after connecting " + reportEntity.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private List<Uri> getPhotosFromDatabase() {
        List<Uri> photosUri = new ArrayList<>();
        Log.e(TAG  , "GET PHOTOS" + reportEntity.toString());
        ForeignCollection<PhotoEntity> listPhotosEntities = reportEntity.getReportListPhotos();
        for (PhotoEntity photoEntity : listPhotosEntities) {
            Uri photoUri = photoEntity.getPhotoUri();
            photosUri.add(photoUri);
        }
        return photosUri;
    }

    private String getDescriptionText(){
        return editTextReportDescription.getText().toString();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG , "onBackPressed Called");
        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
        ReportActivity.this.startActivity(intent);
    }
    public void addReport(View view) {
        Log.i(TAG , "Creating report");
        if(getDescriptionText().equals("")) {
                reportEntity.setReportDescription(getDescriptionText());
                reportEntity.setCreationCompleted(true);
                Log.i(TAG, "Created report");
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                ReportActivity.this.startActivity(intent);
        }
        else {
            Toast.makeText(this, "Podaj opis relacji", Toast.LENGTH_SHORT).show();
        }
    }
}

