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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzynierka.DataPickerWrapper;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.MainActivity;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Photo.PhotosAdapter;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class ReportAddActivity extends FragmentActivity {
    final static String TAG = ReportAddActivity.class.getName();
    Button addPhotos;
    TextInputLayout layoutPhotoDescription;
    ImageView mainPhoto;
    RecyclerView recyclerViewPhotosList;
    TextInputEditText editTextReportTitle;
    TextInputEditText editTextReportDescription;
    TextInputEditText editTextReportLocalization;
    TextView textViewDate;
    AddReportViewModel reportViewModel;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private static PhotosAdapter photosAdapter;
    private static final int GALLERY_REQUEST_CODE = 1;
    TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        reportViewModel = ViewModelProviders.of(this).get(AddReportViewModel.class);

        initButtons();


        reportViewModel.getLiveDataPhotosList().observe(this, new Observer<List<Uri>>() {
            @Override
            public void onChanged(@Nullable final List<Uri> photosUri) {
                // Update the cached copy of the words in the adapter.
                photosAdapter.setPhotos(photosUri);
            }
        });
        setPhotosAdapter();
        setPagerAdapter();
        DataPickerWrapper dataPickerWrapper = new DataPickerWrapper(dateTextView);

    }

    private void setPagerAdapter() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPager.setAdapter(pagerAdapter);
    }

    private void setPhotosAdapter(){
        photosAdapter = new PhotosAdapter(ReportAddActivity.this, mainPhoto, reportViewModel, layoutPhotoDescription);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ReportAddActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPhotosList.setLayoutManager(horizontalLayoutManager);
        recyclerViewPhotosList.setAdapter(photosAdapter);
    }
    private void initButtons() {
        layoutPhotoDescription = (TextInputLayout) findViewById(R.id.photo_description_layout);
        editTextReportTitle = (TextInputEditText) findViewById(R.id.addReportEditTextTitle);
        mainPhoto = (ImageView) findViewById(R.id.reportAddImageViewMainPhoto);
        addPhotos = (Button) findViewById(R.id.reportAddButtonAdd);
        recyclerViewPhotosList = (RecyclerView) findViewById(R.id.reportAddRecyclerViewPhotos);
        dateTextView = (TextView) findViewById(R.id.addReportTextViewDate);
        editTextReportLocalization =(TextInputEditText)findViewById(R.id.reportAddEditTextLocalization);
        textViewDate = (TextView) findViewById(R.id.addReportTextViewDate);
        mPager = (ViewPager) findViewById(R.id.reportAddViewPager);
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
                            reportViewModel.addPhoto(mImageUri);

                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    reportViewModel.addPhoto(uri);
                                }
                            } else {
                                Toast.makeText(this, "Nie wybraleś zdjęcia", Toast.LENGTH_LONG).show();
                            }
                            break;
                        }
                    }
            }
    }


    private String getTitleText(){
        return editTextReportTitle.getText().toString();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG , "onBackPressed Called");
        Intent intent = new Intent(ReportAddActivity.this, MainActivity.class);
        ReportAddActivity.this.startActivity(intent);
    }
    public void addReport(View view) {
        Log.i(TAG , "Creating report");
        if(!getTitleText().equals("")) {
                Log.i(TAG, "Created report");
                insertToDatabase(reportViewModel.getPhotosList());
                Intent intent = new Intent(ReportAddActivity.this, MainActivity.class);
                ReportAddActivity.this.startActivity(intent);
        }
        else {
            Toast.makeText(this, "Podaj tytuł relacji", Toast.LENGTH_SHORT).show();
        }
    }
    public void insertToDatabase(List<Uri> photosUri){
        PhotoEntity photoEntity = null;
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReportDate(Calendar.getInstance().getTime());
        reportEntity.setMainPhoto(reportViewModel.getMainPhotoUri());
        Log.i(TAG, "Report " + reportEntity);
        for (Uri uri : photosUri){
            photoEntity = new PhotoEntity();
            photoEntity.setPhotoUri(uri);
            photoEntity.setReportId(reportEntity.getReportId());
            Log.i(TAG, "Photo " + photoEntity);
            reportViewModel.insertPhoto(photoEntity);
        }
        reportViewModel.insertReport(reportEntity);
    }
}

