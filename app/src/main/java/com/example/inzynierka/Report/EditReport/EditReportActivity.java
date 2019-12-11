package com.example.inzynierka.Report.EditReport;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzynierka.Adapters.ViewPagerAdapter;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.FileManager;
import com.example.inzynierka.Report.ListOfReports.ListOfReportsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class EditReportActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    ViewPager mPager;
    private PagerAdapter pagerAdapter;
    FloatingActionButton editReport;
    TabLayout mTableLayout;
    EditReportModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        initView();
        viewModel = ViewModelProviders.of(this).get(EditReportModel.class);
        Intent intent = getIntent();
        int currentReportId = intent.getIntExtra("currentReport", 0);
        viewModel.setCurrentReportId(currentReportId);
        viewModel.getReportById(currentReportId).observe(this, new Observer<ReportEntity>() {
            @Override
            public void onChanged(ReportEntity reportEntity) {
                Log.e("EditReportActivity", reportEntity.toString());
                viewModel.setCurrentReport(reportEntity);
                if(pagerAdapter==null) {
                    setupPagerAdapter();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.editReportAlertDialog)
                    .setTitle(R.string.editReportTitle);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getApplicationContext(), ListOfReportsActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            TextView tv = (TextView)dialog.getWindow().findViewById(android.R.id.message);
            TextView tv1 = (TextView)dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            TextView tv2 = (TextView)dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.small_text_size));
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.small_text_size));
        }
        else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    private void initView(){
        mPager = (ViewPager) findViewById(R.id.reportAddViewPager);
        editReport = (FloatingActionButton)findViewById(R.id.addReportFloatingButton);
        mTableLayout = (TabLayout) findViewById(R.id.addReportTabLayout);
        editReport.setOnClickListener(this);
        mTableLayout.setupWithViewPager(mPager);
    }
    private void setupPagerAdapter(){
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addReportFloatingButton:
                editReport();
                break;

        }
    }
    private void editReport() {
        if (viewModel.getCurrentReport().getReportTitle() != null) {
            copyImages();
            updateDatabase();
            Intent intent = new Intent(EditReportActivity.this, ListOfReportsActivity.class);
            EditReportActivity.this.startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Podaj tytuł relacji", Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
            toast.show();
        }
        }
    private void copyImages() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

        }
        else {
            FileManager fileManager = new FileManager(this);
            fileManager.setImagesToCopy(viewModel.getPhotosToInsert());
            fileManager.saveImage();
        }
    }
    private void updateDatabase() {
        viewModel.updateReport();

        for(PhotoEntity photoEntity: viewModel.getPhotosToInsert()) {
            Log.e("Dodawane zdjecie", photoEntity.toString());
            viewModel.insertPhoto(photoEntity);
        }

        for(PhotoEntity photoEntity: viewModel.getPhotosToDelete()){
            viewModel.deletePhoto(photoEntity);
        }
        for(PhotoEntity photoEntity: viewModel.getPhotosCurrent().getValue()){
            viewModel.updatePhoto(photoEntity);
        }

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
