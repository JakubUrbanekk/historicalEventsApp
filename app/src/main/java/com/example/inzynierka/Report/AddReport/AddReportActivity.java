package com.example.inzynierka.Report.AddReport;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzynierka.Adapters.ViewPagerAdapter;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.MainActivity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.FileManager;
import com.example.inzynierka.Report.ListOfReports.ListOfReportsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AlertDialog;
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
    FileManager fileManager;
    TabLayout tabLayoutOptionsTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        setSupportActionBar((Toolbar) findViewById(R.id.addReportToolbar));

        reportViewModel = ViewModelProviders.of(this).get(AddReportViewModel.class);
        Log.i(TAG, reportViewModel.toString());

        mPager = (ViewPager) findViewById(R.id.reportAddViewPager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(pagerAdapter);
        addReport = (FloatingActionButton) findViewById(R.id.addReportFloatingButton);
        addReport.setOnClickListener(this);
        mPager.addOnPageChangeListener(this);
        tabLayoutOptionsTab = (TabLayout) findViewById(R.id.addReportTabLayout);
        tabLayoutOptionsTab.setupWithViewPager(mPager);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AddReport);
            builder.setMessage(R.string.addReportAlertDialog)
                    .setTitle(R.string.addReportTitle);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                FileManager fileManager = new FileManager(this);
                fileManager.setImagesToCopy(reportViewModel.getPhotosList());
                fileManager.saveImage();
            }
            insertToDatabase(reportViewModel.getPhotosList());
            Intent intent = new Intent(AddReportActivity.this, ListOfReportsActivity.class);
            AddReportActivity.this.startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Podaj tytuł relacji", Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
            toast.show();
        }
    }

    public void insertToDatabase(List<PhotoEntity> photosEntites) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReportDate(reportViewModel.getReportDate());
        reportEntity.setMainPhoto(reportViewModel.getMainPhotoUri());
        reportEntity.setReportTitle(reportViewModel.getReportTitle());
        reportEntity.setReportLocalization(reportViewModel.getReportLocalization());

        Executor mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long reportId = reportViewModel.insertReport(reportEntity);
                for (PhotoEntity photoEntity : photosEntites) {
                    Log.i(TAG, "Photo " + photoEntity);
                    photoEntity.setReportId((int)(long)(reportId));
                    reportViewModel.insertPhoto(photoEntity);
                }
            }
        });

        Log.i(TAG, "Report " + reportEntity);

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
    private Date pickDateFromFragment(){
        return null;
    }


/*    private void saveImage() {

        file = new File(getExternalFilesDir(null)
                + File.separator + IMAGE_DIRECTORY +"/");

        Log.e("Image directory ", file.toString());

        if(!file.exists())
        file.mkdirs();

        List<PhotoEntity> photosEntites = reportViewModel.getPhotosList();

        int cut = 0;
        String result = null;
        int i=0;

        for (PhotoEntity photoEntity : photosEntites) {
            Uri uri = photoEntity.getPhotoUri();
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

    }
        private String getRealPathFromURI (Uri contentUri){
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(contentUri, projection, null, null, null);
            startManagingCursor(cursor);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
*/
    }

