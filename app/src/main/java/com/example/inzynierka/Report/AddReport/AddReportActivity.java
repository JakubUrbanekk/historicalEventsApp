package com.example.inzynierka.Report.AddReport;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.ListOfReports.ListOfReportsActivity;
import com.example.inzynierka.Report.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class AddReportActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static final String IMAGE_DIRECTORY = "Fotki";

    final static String TAG = AddReportActivity.class.getName();
    AddReportViewModel reportViewModel;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    FloatingActionButton addReport;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        setSupportActionBar((Toolbar) findViewById(R.id.addReportToolbar));


        reportViewModel = ViewModelProviders.of(this).get(AddReportViewModel.class);
        mPager = findViewById(R.id.reportAddViewPager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        addReport = (FloatingActionButton) findViewById(R.id.addReportFloatingButton);
        addReport.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.reportAddViewPager);
        mPager.addOnPageChangeListener(this);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void addReport(View view) {
        Log.i(TAG, "Creating report");
        if (reportViewModel.getReportTitle() != null) {
            Log.i(TAG, "Created report");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

            }
            else {
                saveImage();
            }
            insertToDatabase(reportViewModel.getPhotosList());
            Intent intent = new Intent(AddReportActivity.this, ListOfReportsActivity.class);
            AddReportActivity.this.startActivity(intent);
        } else {
            Toast.makeText(this, "Podaj tytuł relacji", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertToDatabase(List<PhotoEntity> photosEntites) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReportDate(Calendar.getInstance().getTime());
        reportEntity.setMainPhoto(reportViewModel.getMainPhotoUri());
        reportEntity.setReportTitle(reportViewModel.getReportTitle());
        reportEntity.setReportLocalization(reportViewModel.getReportLocalization());
        Log.i(TAG, "Report " + reportEntity);
        for (PhotoEntity photoEntity : photosEntites) {
            Log.i(TAG, "Photo " + photoEntity);
            photoEntity.setReportId(reportEntity.getReportId());
            reportViewModel.insertPhoto(photoEntity);
        }
        reportViewModel.insertReport(reportEntity);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, view.getId() + "");
        switch (view.getId()) {
            case R.id.addReportFloatingButton:
                addReport(view);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_toPhotosFragment:
                break;
            case R.id.toolbar_cancel:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void saveImage() {

        file = new File(getExternalFilesDir(null)
                + File.separator + IMAGE_DIRECTORY +"/");
        Log.e("Image directory ", file.toString());
        if(!file.exists())
        file.mkdirs();

        List<PhotoEntity> photosEntites = reportViewModel.getPhotosList();
        List<Uri> photosUri = new ArrayList<>();
        List<File> destinationFiles = new ArrayList<>();
        List<File> sourceFiles = new ArrayList<>();

        for (PhotoEntity photoEntity : photosEntites) {
            photosUri.add(photoEntity.getPhotoUri());
        }

        int cut = 0;
        String result = null;
        int i=0;

        for (Uri uri : photosUri) {
            sourceFiles.add(new File(getRealPathFromURI(uri)));
            result = uri.getPath();
            cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
            try {
                File copiedFile = copyFile(new File(getRealPathFromURI(uri)), file, File.separator + result);
                photosEntites.get(i).setPhotoUri(Uri.fromFile(copiedFile));
                Log.e("W photo enetites mam ", photosEntites.get(i).getPhotoUri()+"");
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }


          /*  File destinationFile = new File(file, "img_" + result + "");
            destinationFile.mkdirs();
            try {
                destinationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            destinationFiles.add(file);
        }
        try {
            for (int i = 0; i < sourceFiles.size(); i++)
                copyFile(sourceFiles.get(i), destinationFiles.get(i));
        } catch (IOException e) {
            e.printStackTrace();
        */
        }

    }

    private File copyFile(File sourceFile, File destFile, String result) throws IOException {
       if (!sourceFile.exists()) {
            return null;
        }
        File outputFile = new File(destFile, result);
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(outputFile);
        Log.e("Source file ", sourceFile+"");
        Log.e("Zapisuje do:", outputFile+"");
        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;

        return outputFile;
      /*  FileChannel source = null;
        FileChannel destination = null;
        File outputFile = new File(destFile, result);

        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(outputFile).getChannel();
        Log.e("Source file ", sourceFile+"");
        Log.e("Zapisuje do:", outputFile+"");
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

       */
    }
        private String getRealPathFromURI (Uri contentUri){
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(contentUri, projection, null, null, null);
            startManagingCursor(cursor);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

    }

