package com.example.inzynierka.Report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzynierka.Adapters.FullScreenImageAdapter;
import com.example.inzynierka.Adapters.FullScreenVideoAdapter;
import com.example.inzynierka.Adapters.PhotosAdapter;
import com.example.inzynierka.Adapters.RecordingAdapter;
import com.example.inzynierka.Adapters.RecyclerViewItemClickListener;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Database.equipment.accessories.AccessoriesRepository;
import com.example.inzynierka.Database.equipment.clothes.ClothRepository;
import com.example.inzynierka.Database.equipment.vehicles.VehicleRepository;
import com.example.inzynierka.Database.equipment.weapons.WeaponRepository;
import com.example.inzynierka.Database.recordings.RecordingEntity;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.ListOfReports.ListOfReportsActivity;
import com.example.inzynierka.addons.CityPicker;
import com.example.inzynierka.addons.CustomAlertDialog;
import com.example.inzynierka.addons.FinalVariables;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import lombok.AllArgsConstructor;

@lombok.extern.java.Log
public abstract class EditableReportActivity extends OpenPhotoGalleryActivity implements View.OnClickListener, RecyclerViewItemClickListener , ActionBottomDialogFragment.ItemClickListener{
    public FloatingActionButton addReport;
    ViewPager vpPhotos;
    ViewPager vpVideos;
    ConstraintLayout clContent;
    ImageView bAddPhotos;
    RecyclerView rvPhotos;
    private ReportRepository repository;
    RecyclerView rvVideos;
    protected RecyclerView rvRecordings;
    protected Spinner spinnerCategory;
    protected Spinner spinnerEpoka;
    protected Spinner spinnerClothes;
    protected Spinner spinnerWeapon;
    protected Spinner spinnerAccessory;
    protected Spinner spinnerVehicle;
    protected TextView tvDate;
    protected TextInputEditText etLocalization;
    protected TextInputEditText etTitle;
    protected PhotosAdapter aPhotoAdapter;
    protected PhotosAdapter aVideoAdapter;
    protected RecordingAdapter aRecordingAdapter;
    protected FullScreenImageAdapter aFullScreenPhotos;
    FullScreenVideoAdapter aFullScreenVideos;
    protected TextInputEditText etDescription;
    private ImageView bAddVideo;
    private ImageView bAddRecording;
    ActionBottomDialogFragment addPhotoBottomDialogFragment;
    protected LinearLayout photoPane;
    protected LinearLayout recordingPane;
    protected LinearLayout videoPane;
    private final String PHOTO_GALLERY = "Dodaj zdjęcie z galerii";
    private final String PHOTO_CAMERA = "Zrób zdjęcie";
    private final String VIDEO_GALLERY = "Dodaj film z galerii";
    private final String VIDEO_CAMERA ="Nagraj film";
    private final String VIDEO_ADAPTER = "Video adapter";
    private final String PHOTO_ADAPTER ="Photo adapter";
    private final int SPINNER_CATEGORY = 1;
    private final int SPINNER_EPOKA = 2;
    private final int SPINNER_CLOTH = 3;
    private final int SPINNER_WEAPON = 4;
    private final int SPINNER_ACCESSORY = 5;
    private final int SPINNER_POJAZD = 6;
    CustomAlertDialog customAlertDialog;
    private final int PERMISSION_ID = 202;
    FusedLocationProviderClient mFusedLocationClient;
    Activity currentActivity;
    ClothRepository clothRepository;
    VehicleRepository vehicleRepository;
    AccessoriesRepository accessoryRepository;
    WeaponRepository weaponRepository;
    int currentReportId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        customAlertDialog = new CustomAlertDialog(this);
        Intent intent = getIntent();
        currentReportId = intent.getIntExtra("currentReport", -1);
        currentActivity = this;
        repository = new ReportRepository(getApplication());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
         clothRepository = new ClothRepository(getApplication());
         vehicleRepository = new VehicleRepository(getApplication());
         accessoryRepository = new AccessoriesRepository(getApplication());
         weaponRepository = new WeaponRepository(getApplication());
        initView();
    }

    @Override
    protected void onPhotosAdded(List<PhotoEntity> photoEntities) {
        Log.e("Editable Report Activity", "Using observer on addPhoto");
        if(photoEntities.size()>0){
            photoPane.setVisibility(View.VISIBLE);
        }
        else {
            photoPane.setVisibility(View.GONE);
        }
        aPhotoAdapter.setPhotos(photoEntities);
        aFullScreenPhotos.setPhotos(photoEntities);
    }

    @Override
    protected void onRecordingAdded(List<RecordingEntity> photoEntities) {
        log.info("Adding recording");
        if(photoEntities.size()>0){
            recordingPane.setVisibility(View.VISIBLE);
        }
        else {
            recordingPane.setVisibility(View.GONE);
        }
        aRecordingAdapter.setRecordings(photoEntities);
    }

    @Override
    protected void onVideosAdded(List<VideoEntity> videoEntities) {
        if(videoEntities.size()>0){
            videoPane.setVisibility(View.VISIBLE);
        }
        else {
            videoPane.setVisibility(View.GONE);
        }
        aVideoAdapter.setVideos(videoEntities);
        aFullScreenVideos.setVideos(videoEntities);
    }

    private void initView(){
        //setSupportActionBar((Toolbar) findViewById(R.id.addReportToolbar));
        addReport = (FloatingActionButton) findViewById(R.id.addReportFloatingButton);
        bAddPhotos = (ImageView) findViewById(R.id.addReportAddPhotoB);
        vpPhotos = (ViewPager) findViewById(R.id.addReportVP);
        vpVideos = (ViewPager) findViewById(R.id.addReportVideoVP);
        clContent = (ConstraintLayout) findViewById(R.id.addReportCL);
        rvPhotos = (RecyclerView) findViewById(R.id.addReportShowPhotosRV);
        rvVideos = (RecyclerView) findViewById(R.id.addReportShowVideosRV);
        rvRecordings = (RecyclerView) findViewById(R.id.addReportShowRecordingRV);
        tvDate = (TextView) findViewById(R.id.addReportTextViewDate);
        etTitle = (TextInputEditText) findViewById(R.id.addReportEditTextTitle);
        bAddRecording = (ImageView) findViewById(R.id.addSoundButton);
        bAddRecording.setOnClickListener(this);
        spinnerCategory = (Spinner) findViewById(R.id.addReportSpinnerCategory);
        spinnerEpoka = (Spinner) findViewById(R.id.addReportSpinnerEpoka);
        spinnerAccessory = (Spinner) findViewById(R.id.addReportSpinnerAccessory);
        spinnerClothes = (Spinner) findViewById(R.id.addReportSpinnerClothes);
        spinnerVehicle = (Spinner) findViewById(R.id.addReportSpinnerVehicle);
        spinnerWeapon = (Spinner) findViewById(R.id.addReportSpinnerWeapon);
        setEtTitleText();
        etLocalization = (TextInputEditText) findViewById(R.id.reportAddEditTextLocalization);
        etDescription = (TextInputEditText) findViewById(R.id.reportAddEditTextReportDescription);
        bAddVideo = (ImageView) findViewById(R.id.addVideoButton);
        DataPickerWrapper dataPickerWrapper = new DataPickerWrapper(tvDate, this);
        photoPane = (LinearLayout) findViewById(R.id.photosPane);
        videoPane = (LinearLayout) findViewById(R.id.videoPane);
        recordingPane = (LinearLayout) findViewById(R.id.recordingPane);
        bAddPhotos.setOnClickListener(this);
        addReport.setOnClickListener(this);
        bAddVideo.setOnClickListener(this);
        initSpinners();
        initAdapters();
    }
    private void initSpinners(){
        initSpinner(spinnerCategory, SPINNER_CATEGORY);
        initSpinner(spinnerEpoka, SPINNER_EPOKA);
        initSpinner(spinnerAccessory, SPINNER_ACCESSORY);
        initSpinner(spinnerClothes, SPINNER_CLOTH);
        initSpinner(spinnerVehicle, SPINNER_POJAZD);
        initSpinner(spinnerWeapon, SPINNER_WEAPON);
    }

    protected void initSpinner(Spinner spinner, int requestCode) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_cheked_item_small,
                new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        initSpinnerValues(spinnerAdapter, requestCode);
        addDefaultListenerToSpinner(spinner, spinnerAdapter.getCount());
    }
    private String getSpinnerSelection(Spinner spinner){
        return spinner.getSelectedItem().toString();
    }
    protected String getCategorySpinnerValue(){
        return getSpinnerSelection(spinnerCategory);
    }
    protected String getEpokaSpinnerValue(){
        return getSpinnerSelection(spinnerEpoka);
    }
    protected String getClothSpinnerValue(){
        return getSpinnerSelection(spinnerClothes);
    }
    protected String getVehicleSpinnerValue(){
        return getSpinnerSelection(spinnerVehicle);
    }
    protected String getAccessorySpinnerValue(){
        return getSpinnerSelection(spinnerAccessory);
    }
    protected String getWeaponSpinnerValue(){
        return getSpinnerSelection(spinnerWeapon);
    }

    private void initSpinnerValues(SpinnerAdapter spinnerAdapter, int requestCode) {
        switch (requestCode){
            case SPINNER_CATEGORY:
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Nieokreślona", 0);
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Rekonstrukcja bitewna", spinnerAdapter.getCount());
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Walka taktyczna", spinnerAdapter.getCount());
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Rekonstrukcja komercyjna", spinnerAdapter.getCount());
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "\"Żywa historia\"", spinnerAdapter.getCount());
                new GetCategoriesAsyncTask(spinnerAdapter.getCount(), (ArrayAdapter<String>)spinnerAdapter).execute();
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Dodaj nową", spinnerAdapter.getCount());
                break;
            case SPINNER_EPOKA:
                List<String> epoki = Arrays.asList("Prehistoria", "Starożytność", "Średniowiecze", "Czasy nowożytne");
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Nieokreślona", 0);
                epoki.forEach(epoka ->
                        addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, epoka, spinnerAdapter.getCount()));
                new GetEpokaAsyncTask(spinnerAdapter.getCount(), (ArrayAdapter<String>)spinnerAdapter, epoki).execute();
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Dodaj nową", spinnerAdapter.getCount());
                break;
            case SPINNER_ACCESSORY:
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, FinalVariables.ACCESSORY_NOT_SELECTED_CONST, 0);
                new GetAccessoriesseAsyncTask(spinnerAdapter.getCount(), (ArrayAdapter<String>)spinnerAdapter).execute();
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Dodaj nowe", spinnerAdapter.getCount());
                break;
            case SPINNER_CLOTH:
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, FinalVariables.CLOTH_NOT_SELECTED_CONST, 0);
                new GetClothAsyncTask(spinnerAdapter.getCount(), (ArrayAdapter<String>)spinnerAdapter).execute();
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Dodaj nowy", spinnerAdapter.getCount());
                break;
            case SPINNER_POJAZD:
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, FinalVariables.VEHICLE_NOT_SELECTED_CONST, 0);
                new GetVehicleAsyncTask(spinnerAdapter.getCount(), (ArrayAdapter<String>)spinnerAdapter).execute();
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Dodaj nowy", spinnerAdapter.getCount());
                break;
            case SPINNER_WEAPON:
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, FinalVariables.WEAPON_NOT_SELECTED_CONST, 0);
                new GetWeaponAsyncTask(spinnerAdapter.getCount(), (ArrayAdapter<String>)spinnerAdapter).execute();
                addTextToSpinner((ArrayAdapter<String>) spinnerAdapter, "Dodaj nową", spinnerAdapter.getCount());
                break;
            default:
                break;
        }
    }

    private void addTextToSpinner(ArrayAdapter<String> adapter, String text, int position){
        if(text == null || text.equals("Nieokreślona") && position != 0){
            return;
        }
        List<String> spinnerOptions = getSpinnerOptions(adapter);
        String lowerText = text.toLowerCase();
        if(!spinnerOptions.contains(lowerText)) {
            text = StringUtils.capitalize(text);
            adapter.insert(text,position);
            adapter.notifyDataSetChanged();
        }
    }

    private List<String> getSpinnerOptions(ArrayAdapter<String> adapter) {
        int size = adapter.getCount();
        List<String> arrayList = new ArrayList<>();
        for (int i=0 ;i<size-1; i++){
            arrayList.add(adapter.getItem(i).toLowerCase());
        }
        return arrayList;
    }

    private void addDefaultListenerToSpinner(Spinner spinner, int count){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals("Dodaj nową") || spinner.getSelectedItem().toString().equals("Dodaj nowy") || spinner.getSelectedItem().toString().equals("Dodaj nowe")){
                    createDialogBoxWithEditText(spinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createDialogBoxWithEditText(Spinner spinner){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("Podaj tytuł");
        alert.setView(edittext);
        alert.setPositiveButton("Dodaj", (dialog, whichButton) -> {
            //What ever you want to do with the value
            //OR
            String editTextValue = edittext.getText().toString();
            addTextToSpinner((ArrayAdapter<String>) spinner.getAdapter(), editTextValue, spinner.getCount() -1);
            spinner.setSelection(spinner.getAdapter().getCount()-2);
        });
        alert.setNegativeButton("Anuluj", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            spinner.setSelection(spinner.getAdapter().getCount()-2);
        });
        alert.show();
    }

    protected void setEtTitleText(){
        Date today = Calendar.getInstance().getTime();
        String etText = "RELACJA_"+today;
        etTitle.setText(etText);
    }

    private void initAdapters(){
        initPhotosAdapter();
        initVideosAdapter();
        initRecordingAdapter();
        initFullscreenPhotosAdapter();
        initFullScreenVideosAdapter();
    }

    private void initFullScreenVideosAdapter(){
        aFullScreenVideos = new FullScreenVideoAdapter(this);
        List<VideoEntity> currentVideos = videoModel.getVideoList();
        if (currentVideos!=null){
            aFullScreenVideos.setVideos(currentVideos);
        }
        vpVideos.setAdapter(aFullScreenVideos);
    }

    private void initFullscreenPhotosAdapter() {
        aFullScreenPhotos = new FullScreenImageAdapter(this);
        List<PhotoEntity> currentPhotos = viewModel.getPhotosList();
        if (currentPhotos!=null){
            aFullScreenPhotos.setPhotos(currentPhotos);
        }
        vpPhotos.setAdapter(aFullScreenPhotos);
    }

    private void initPhotosAdapter(){
        aPhotoAdapter = new PhotosAdapter(this, this, PHOTO_ADAPTER);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(horizontalLayoutManager);
        rvPhotos.setAdapter(aPhotoAdapter);
    }

    private void initVideosAdapter() {
        aVideoAdapter = new PhotosAdapter(this, this, VIDEO_ADAPTER);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvVideos.setLayoutManager(horizontalLayoutManager);
        rvVideos.setAdapter(aVideoAdapter);
}
    private void initRecordingAdapter() {
        aRecordingAdapter = new RecordingAdapter();
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRecordings.setLayoutManager(horizontalLayoutManager);
        rvRecordings.setAdapter(aRecordingAdapter);
    }

    @Override
    public void onBackPressed(){
        if (vpPhotos.getVisibility() == View.VISIBLE) {
            hideViewPager();
        }
        else if (vpVideos.getVisibility() == View.VISIBLE){
            hideVideoPager();
        }
        else {
            int titleID = setAlertDialogTitle();
            String messageID = setAlertDialogMessage();
            customAlertDialog.createAlertDialog(titleID, messageID);
        }
    }

    protected abstract String setAlertDialogMessage();

    protected abstract int setAlertDialogTitle();

    private void showViewPager() {
        Log.e("Open view pager ", "Opened");
        vpPhotos.setVisibility(View.VISIBLE);
        clContent.setVisibility(View.GONE);
    }

    private void showVideoViewPager(){
        vpVideos.setVisibility(View.VISIBLE);
        clContent.setVisibility(View.GONE);
    }

    private void hideViewPager(){
        vpPhotos.setVisibility(View.GONE);
        clContent.setVisibility(View.VISIBLE);
    }

    private void hideVideoPager(){
        vpVideos.setVisibility(View.GONE);
        clContent.setVisibility(View.VISIBLE);
        aFullScreenVideos.getVideoViews().forEach(videoView -> {
            if(videoView.isPlaying()){
                videoView.stopPlayback();
            }
        });
    }

    @Override
    public void onClick(View view){
            switch (view.getId()) {
                case R.id.addReportFloatingButton:
                    updateReport(view);
                    break;
                case R.id.addReportAddPhotoB:
                    showBottomSheet(view,  PHOTO_CAMERA, PHOTO_GALLERY);
                    break;
                case R.id.addVideoButton:
                    showBottomSheet(view,  VIDEO_CAMERA, VIDEO_GALLERY);
                    break;
                case R.id.addSoundButton:
                    openDicaphone(DICAPHONE_REQUEST_CODE);

            }
    }

    public void showBottomSheet(View view, String topText, String bottomText) {
        addPhotoBottomDialogFragment =
                 new ActionBottomDialogFragment(topText, bottomText);

        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
               "Asd");

    }

    @Override
    public void recyclerViewListClicked(View v, int position, String id) {
        Log.e("VP Opened", id);

        if (id.equals(PHOTO_ADAPTER)) {
            vpPhotos.setCurrentItem(position);
            showViewPager();
        } else if (id.equals(VIDEO_ADAPTER)){
            vpVideos.setCurrentItem(position);
            showVideoViewPager();

        }
    }

    private String getETText(TextInputEditText textInputEditText){
        Editable editText = textInputEditText.getText();
        return editText != null && !editText.toString().equals("") ? editText.toString() : null;
    }

    public String getReportDescription(){
        return getETText(etDescription);
    }

    public String getReportTitle(){
        return getETText(etTitle);
    }

    public String getReportLocalization(){
        return getETText(etLocalization);
    }

    private String getTextViewText(TextView textView){
        CharSequence tv = textView.getText();
        return tv != null ? tv.toString() : null;
    }

    public String getReportDate(){
        return getTextViewText(tvDate);
    }

    private void updateReport(View view) {
        if (getReportTitle() != null) {
            copyImages();
            copyVideos();
            updateDatabase();
            new AddNewEquipmentAsyncTask().execute();
            Intent intent = new Intent(this, ListOfReportsActivity.class);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Podaj tytuł relacji", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    protected abstract void updateDatabase();

    private void copyImages() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

        }
        else {
            FileManager fileManager = new FileManager(this);
            fileManager.setImagesToCopy(viewModel.getPhotosList());
            fileManager.saveImages();
        }
    }
    private void copyVideos(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

        }
        else {
            FileManager fileManager = new FileManager(this);
            fileManager.setVideosToCopy(videoModel.getVideoList());
            fileManager.saveVideos();
        }
    }

    @Override
    public void onItemClick(View item) {
        switch (item.getId()){
            case R.id.barBottomTvGallery:
                if(addPhotoBottomDialogFragment.getTopText().equals(PHOTO_GALLERY))
                    openGallery(item, GALLERY_REQUEST_CODE);
                else {
                    openGallery(item, GALLERY_VIDEO_REQUEST_CODE);
                }
                break;
            case R.id.barBottomTvCamera:
                if(addPhotoBottomDialogFragment.getBottomText().equals(PHOTO_CAMERA)) {
                    openCamera(item, CAMERA_REQUEST_CODE);
                }
                else {
                    openCamera(item, VIDEO_REQUEST_CODE);
                }
                break;
            default:
                break;
        }
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }
    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    String cityName = CityPicker.getCityName(currentActivity, location.getLongitude(), location.getLatitude());
                                    etLocalization.setText(cityName);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            String cityName = CityPicker.getCityName(currentActivity, mLastLocation.getLongitude(), mLastLocation.getLatitude());
            etLocalization.setText(cityName);
        }
    };

    public class AddNewEquipmentAsyncTask extends AsyncTask<Void, Void, ReportEntity>{
        @Override
        protected ReportEntity doInBackground(Void... voids) {
            String weapon = getSpinnerSelection(spinnerWeapon);
            String accessory = getSpinnerSelection(spinnerAccessory);
            String vehicle =  getSpinnerSelection(spinnerVehicle);
            String cloth =  getSpinnerSelection(spinnerClothes);
            if (weapon != "Nieokreślona"){
                IEquipment iEquipment = new IEquipment(weapon);
                weaponRepository.insert(iEquipment);
            }
            if (accessory != "Nieokreślony"){
                IEquipment iEquipment = new IEquipment(accessory);
                accessoryRepository.insert(iEquipment);
            }
            if (vehicle != "Nieokreślony"){
                IEquipment iEquipment = new IEquipment(vehicle);
                vehicleRepository.insert(iEquipment);
            }
            if (cloth != "Nieokreślony"){
                IEquipment iEquipment = new IEquipment(cloth);
                clothRepository.insert(iEquipment);
            }
            return null;
        }
    }

    public class GetReportByIdAsyncTask extends AsyncTask<Void, Void, ReportEntity>{
        @Override
        protected ReportEntity doInBackground(Void... voids) {
            if(currentReportId != -1){
                ReportRepository reportRepository = new ReportRepository(getApplication());
                return reportRepository.getReportByIdentifier(currentReportId);
            }
            else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ReportEntity reportEntity) {
            if(reportEntity != null)
            initSpinnerSelectedValues(reportEntity);
        }
    }

    protected void initSpinnerSelectedValues(ReportEntity reportEntity){
        setSpinnerPositionByValue(spinnerCategory, reportEntity.getCategory());
        setSpinnerPositionByValue(spinnerEpoka, reportEntity.getEpoka());
        setSpinnerPositionByValue(spinnerClothes, reportEntity.getCloth());
        setSpinnerPositionByValue(spinnerWeapon, reportEntity.getWeapon());
        setSpinnerPositionByValue(spinnerVehicle, reportEntity.getVehicle());
        setSpinnerPositionByValue(spinnerAccessory, reportEntity.getAccessory());
    }

    private void setSpinnerPositionByValue(Spinner spinner, String value){
        log.info("Selected spinner value " + value);
        if (value != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>)(spinner.getAdapter());
            int spinnerPosition = adapter.getPosition(value);
            if (spinnerPosition != -1) {
                spinnerPosition = adapter.getPosition(value);
                log.info("Selected spinner position " + spinnerPosition);
                spinner.setSelection(spinnerPosition);
            }
        }
    }

    @AllArgsConstructor
    public class GetCategoriesAsyncTask extends AsyncTask<Void, Void, Set<String>> {
        int size;
        ArrayAdapter<String> spinnerAdapter;
        @Override
        protected Set<String> doInBackground(Void... voids) {
            List<ReportEntity> list = repository.getAll();
            List<String> mappedToString = list.stream()
                    .map(report ->report.getCategory())
                    .collect(Collectors.toList());
            Set<String> set = new HashSet<>(mappedToString);
            return set;
        }

        @Override
        protected void onPostExecute(Set<String> list) {
            list.stream()
                    .forEach(string ->
                            addTextToSpinner(spinnerAdapter, string, size));
            new GetReportByIdAsyncTask().execute();
            }
        }
    @AllArgsConstructor
    public class GetEpokaAsyncTask extends AsyncTask<Void, Void, Set<String>> {
        int size;
        ArrayAdapter<String> spinnerAdapter;
        List<String> epoki;
        @Override
        protected Set<String> doInBackground(Void... voids) {
            List<ReportEntity> list = repository.getAll();
            List<String> mappedToString = list.stream()
                    .map(report ->report.getEpoka())
                    .collect(Collectors.toList());
            Set<String> set = new HashSet<>(mappedToString);
            return set;
        }

        @Override
        protected void onPostExecute(Set<String> list) {
            list.stream()
                    .forEach(string ->{
                            if(!epoki.contains(string)){
                            addTextToSpinner(spinnerAdapter, string, size);
                            }
            });
            new GetReportByIdAsyncTask().execute();
        }
    }
    @AllArgsConstructor
    public class GetWeaponAsyncTask extends AsyncTask<Void, Void, Set<String>> {
        int size;
        ArrayAdapter<String> spinnerAdapter;
        @Override
        protected Set<String> doInBackground(Void... voids) {
            List<IEquipment> list = weaponRepository.getAll();
            List<String> mappedToString = list.stream()
                    .map(report ->report.getName())
                    .collect(Collectors.toList());
            Set<String> set = new HashSet<>(mappedToString);
            return set;
        }

        @Override
        protected void onPostExecute(Set<String> list) {
            list.stream()
                    .forEach(string ->
                            addTextToSpinner(spinnerAdapter, string, size));
            new GetReportByIdAsyncTask().execute();
        }
    }
    @AllArgsConstructor
    public class GetClothAsyncTask extends AsyncTask<Void, Void, Set<String>> {
        int size;
        ArrayAdapter<String> spinnerAdapter;
        @Override
        protected Set<String> doInBackground(Void... voids) {
            List<IEquipment> list = clothRepository.getAll();
            List<String> mappedToString = list.stream()
                    .map(report ->report.getName())
                    .collect(Collectors.toList());
            Set<String> set = new HashSet<>(mappedToString);
            return set;
        }

        @Override
        protected void onPostExecute(Set<String> list) {
            log.info("Cloth list " + list);
            list.stream()
                    .forEach(string ->
                            addTextToSpinner(spinnerAdapter, string, size));
            new GetReportByIdAsyncTask().execute();
        }
    }
    @AllArgsConstructor
    public class GetVehicleAsyncTask extends AsyncTask<Void, Void, Set<String>> {
        int size;
        ArrayAdapter<String> spinnerAdapter;

        @Override
        protected Set<String> doInBackground(Void... voids) {
            List<IEquipment> list = vehicleRepository.getAll();
            List<String> mappedToString = list.stream()
                    .map(report ->report.getName())
                    .collect(Collectors.toList());
            Set<String> set = new HashSet<>(mappedToString);
            return set;
        }

        @Override
        protected void onPostExecute(Set<String> list) {
            list.stream()
                    .forEach(string ->
                            addTextToSpinner(spinnerAdapter, string, size));
            new GetReportByIdAsyncTask().execute();
        }
    }
    @AllArgsConstructor
    public class GetAccessoriesseAsyncTask extends AsyncTask<Void, Void, Set<String>> {
        int size;
        ArrayAdapter<String> spinnerAdapter;
        @Override
        protected Set<String> doInBackground(Void... voids) {
            List<IEquipment> list = accessoryRepository.getAll();
            List<String> mappedToString = list.stream()
                    .map(report ->report.getName())
                    .collect(Collectors.toList());
            Set<String> set = new HashSet<>(mappedToString);
            return set;
        }

        @Override
        protected void onPostExecute(Set<String> list) {
            list.stream()
                    .forEach(string ->
                            addTextToSpinner(spinnerAdapter, string, size));
            new GetReportByIdAsyncTask().execute();
        }
    }
}





