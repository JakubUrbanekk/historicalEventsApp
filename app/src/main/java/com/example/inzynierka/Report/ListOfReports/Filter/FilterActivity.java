package com.example.inzynierka.Report.ListOfReports.Filter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.videos.VideoEntity;
import com.example.inzynierka.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, Spinner.OnItemSelectedListener{
    RadioButton rbDate;
    TextInputEditText etTitle;
    Spinner sLocalization;
    RadioGroup rgWhichDateType;
    LinearLayout llMonthAndYear;
    Spinner sDate;
    Spinner sYear;
    Spinner sMonth;
    FloatingActionButton fab;
    FilterModel viewModel;
    Filter filter;
    private CrystalRangeSeekbar rangeSeekbar;
    private CrystalRangeSeekbar videoSeekbar;
    private TextView title;
    private TextView minSeekBar;
    private TextView maxSeekBar;
    private TextView minVideoBar;
    private TextView maxVideoBar;
    private Spinner categorySpinner;
    private Spinner epokaSpinner;
    private Spinner clothSpinner;
    private Spinner weaponSpinner;
    private Spinner accessorySpinner;
    private Spinner vehicleSpinner;
    final String TAG = "FilterActivity";
    public final static String STRING_FILTER_RESULT = "result";
    public final static String STRING_FILTER_EMPTY_RESULT = "empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        viewModel = ViewModelProviders.of(this).get(FilterModel.class);
        createFilterAndInitView();
    }
    private void createFilterAndInitView(){
        Log.e("Filter ", "Create Filter and Init View");
        viewModel.reportRepository.getAllReports().observe(this, new Observer<List<ReportEntity>>() {
            @Override
            public void onChanged(List<ReportEntity> reportEntityList) {
                Log.e("Filter ", "Report entity list changed");
                viewModel.setAllReports(reportEntityList);
                Log.e("Filter ", "Report entity list " + reportEntityList.isEmpty());
                if (!viewModel.getAllReports().isEmpty()){
                    viewModel.setFilter(viewModel.getAllReports(), viewModel.getAllPhotos(), viewModel.getAllVideos());
                    filter = viewModel.getFilter();
                    initView();
                }
            }
        });

        viewModel.photoRepository.getAllPhotos().observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(List<PhotoEntity> photosEntites) {
                viewModel.setAllPhotos(photosEntites);
                if (!viewModel.getAllPhotos().isEmpty()){
                    viewModel.setFilter(viewModel.getAllReports(), viewModel.getAllPhotos(), viewModel.getAllVideos());
                    filter = viewModel.getFilter();
                    initView();
                }
            }
        });
        viewModel.videoRepository.getAllVideos().observe(this, new Observer<List<VideoEntity>>() {
            @Override
            public void onChanged(List<VideoEntity> videoEntities) {
                viewModel.setAllVideos(videoEntities);
                if (!viewModel.getAllVideos().isEmpty()){
                    viewModel.setFilter(viewModel.getAllReports(), viewModel.getAllPhotos(), viewModel.getAllVideos());
                    filter = viewModel.getFilter();
                    initView();
                }
            }
        });
    }

    private void initView() {
        Log.e("Filter ", "Initing view");
        etTitle = findViewById(R.id.filterETpickTitle);
        sLocalization = findViewById(R.id.filterSPickLocalization);
        rgWhichDateType = findViewById(R.id.filterRGWhichDate);
        sDate = findViewById(R.id.filterSPickDate);
        sYear = findViewById(R.id.filterSPickYear);
        sMonth = findViewById(R.id.filterSPickMonth);
        llMonthAndYear = findViewById(R.id.filterLLMonthAndYear);
        title = findViewById(R.id.reportTitleTV);
        title.setText("Filtr");
        fab = findViewById(R.id.addReportFloatingButton);
        fab.setOnClickListener(this);
        rbDate = findViewById(R.id.filterRBPickDate);
        rbDate.setChecked(true);
        rgWhichDateType.setOnCheckedChangeListener(this);
        sYear.setOnItemSelectedListener(this);
        accessorySpinner = findViewById(R.id.filterSpinnerAccessory);
        initSpinnerWithList(accessorySpinner, viewModel.getReportsAccessoryForSpinner());
        categorySpinner = findViewById(R.id.filterSpinnerCategory);
        initSpinnerWithList(categorySpinner, viewModel.getReportsCategoryForSpinner());
        epokaSpinner = findViewById(R.id.filterSpinnerEpoka);
        initSpinnerWithList(epokaSpinner, viewModel.getReportsEpokaForSpinner());
        clothSpinner = findViewById(R.id.filterSpinnerCloth);
        initSpinnerWithList(clothSpinner, viewModel.getReportsClothsForSpinner());
        weaponSpinner = findViewById(R.id.filterSpinnerWeapon);
        initSpinnerWithList(weaponSpinner, viewModel.getReportsWeaponssForSpinner());
        vehicleSpinner = findViewById(R.id.filterSpinnerVehicle);
        initSpinnerWithList(vehicleSpinner, viewModel.getReportsVehiclesForSpinner());
        initDateSpinner();
        initLocationSpinner();
        initYearSpinner();
        initMonthSpinner();
        initPhotoSeekBar();
        initVideoSeekBar();
    }

    private void initVideoSeekBar() {
        videoSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbarVideos);
        minVideoBar = (TextView) findViewById(R.id.minVideoSeekBar);
        maxVideoBar = (TextView) findViewById(R.id.maxVideoSeekBar);
        videoSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minVideoBar.setText(String.valueOf(minValue));
                maxVideoBar.setText(String.valueOf(maxValue));
            }
        });
        int maxVideosAmount = viewModel.getMaxVideosAmount();
        Log.e("Filter", "Max video amount " + maxVideosAmount);
        videoSeekbar.setMaxStartValue(maxVideosAmount);
        videoSeekbar.setMaxValue(maxVideosAmount);
        maxVideoBar.setText(""+maxVideosAmount);
    }

    private void initPhotoSeekBar() {
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        minSeekBar = (TextView) findViewById(R.id.minPhotoSeekBar);
        maxSeekBar = (TextView) findViewById(R.id.maxPhotoSeekBar);
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minSeekBar.setText(String.valueOf(minValue));
                maxSeekBar.setText(String.valueOf(maxValue));
            }
        });
        int maxPhotoAmount = viewModel.getMaxPhotosAmount();
        Log.e("Filter", "Max photo amount " + maxPhotoAmount);
        rangeSeekbar.setMaxStartValue(maxPhotoAmount);
        rangeSeekbar.setMaxValue(maxPhotoAmount);
        maxSeekBar.setText(""+maxPhotoAmount);
    }

    private void initLocationSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_cheked_item_small, viewModel.getReportsLocalizationsForSpinner());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sLocalization.setAdapter(adapter);
    }

    private void initSpinnerWithList(Spinner spinner, Set<String> items){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_cheked_item_small, new ArrayList<>(items));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initYearSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_cheked_item_small, viewModel.getReportsYears());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sYear.setAdapter(adapter);
    }

    private void initMonthSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_cheked_item_small, viewModel.getReportsMonths(sYear.getSelectedItem().toString()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMonth.setAdapter(adapter);
    }

    private void changeMonthSpinnerDataBySelectedYear(String year){
        List <String> newMonths = viewModel.getReportsMonths(year);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_cheked_item_small, newMonths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMonth.setAdapter(adapter);
    }

    private void initDateSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_cheked_item_small, viewModel.getTextForDateSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDate.setAdapter(adapter);
    }

    private String getEtTitleText(){
        return getEtText(etTitle);
    }

    private String getEtMinPhotosText(){
        return getTvText(minSeekBar);
    }

    private String getTvMinVideosText() {
        return getTvText(minVideoBar);
    }

    private String getEtMaxPhotosText(){
        return getTvText(maxSeekBar);
    }

    private String getEtText(TextInputEditText textInputEditText) {
        if (textInputEditText.getText() != null && !textInputEditText.getText().toString().equals("")) {
            return textInputEditText.getText().toString();
        }
        else return null;
    }

    private String getTvText(TextView textView){
        if (textView.getText() != null && !textView.getText().toString().equals("")) {
            return textView.getText().toString();
        }
        else return null;
     }


    private String getSpinnerText(Spinner spinner){
        return spinner.getSelectedItem().toString();
    }

    private String getSLocationText(){
        return getSpinnerText(sLocalization);
    }

    private String getSDateText(){
        return getSpinnerText(sDate);
    }

    private String getSYearText(){
        return getSpinnerText(sYear);
    }

    private String getSMonth(){
        return getSpinnerText(sMonth);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addReportFloatingButton:
                Log.e(TAG, "Fab clicked");
                List <ReportEntity> filterResult = getFilterResult();
                returnToListIntentCreator(filterResult);
                break;
        }
    }

    private void returnToListIntentCreator(List<ReportEntity> filterResult) {
        Log.e(TAG, "returning fitler list...");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (filterResult == Filter.LIST_EMPTY_RESULT){
            Log.e(TAG, "Filter return empty list");
            bundle.putString(STRING_FILTER_EMPTY_RESULT, STRING_FILTER_EMPTY_RESULT);
        }
        ArrayList<ReportEntity> result = new ArrayList<>(filterResult);
        bundle.putParcelableArrayList(STRING_FILTER_RESULT, result);

        result.stream()
                .forEach(reportEntity -> Log.e(TAG, reportEntity.toString()));
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private List<ReportEntity> getFilterResult() {
        String title = getEtTitleText();
        String minPhotos = getEtMinPhotosText();
        String maxPhotos = getEtMaxPhotosText();
        String minVideos = getTvMinVideosText();
        String maxVideos = getTvMaxVideosText();
        String month = getSMonth();
        String year = getSYearText();
        String date = getSDateText();
        String location = getSLocationText();
        String category = getSpinnerText(categorySpinner);
        String epoka = getSpinnerText(epokaSpinner);
        String cloth = getSpinnerText(clothSpinner);
        String vehicle = getSpinnerText(vehicleSpinner);
        String accessory = getSpinnerText(accessorySpinner);
        String weapon = getSpinnerText(weaponSpinner);
        List<ReportEntity> filtredByTitle;
        List<ReportEntity> filtredByMinPhotos;
        List<ReportEntity> filtredByMaxPhotos;
        List<ReportEntity> filtredByMaxVideos;
        List<ReportEntity> filterdByMinVideos;
        List<ReportEntity> filtredByMonthAndYear;
        List<ReportEntity> filtredByYear;
        List<ReportEntity> filtredByDate;
        List<ReportEntity> filtredByLocation;
        List<ReportEntity> result;
        List<List<ReportEntity>> filtredLists = new ArrayList<>();

        if(title!=null) {
            filtredByTitle = filter.getReportsByReportTitle(title);
            filtredLists.add(filtredByTitle);
        }

        if(minPhotos!=null){
            int iMinPhotos = Integer.parseInt(minPhotos);
            filtredByMinPhotos = filter.getReportsWithMinimumPhotosAmount(iMinPhotos);
            filtredLists.add(filtredByMinPhotos);
        }
        if(maxPhotos!=null){
            int iMaxPhotos = Integer.parseInt(maxPhotos);
            filtredByMaxPhotos = filter.getReportsWithMaxPhotosAmount(iMaxPhotos);
            filtredLists.add(filtredByMaxPhotos);
        }

        if(minVideos!=null){
            int iMinVideos = Integer.parseInt(minVideos);
            filterdByMinVideos = filter.getReportsWithMinimumVideosAmount(iMinVideos);
            filtredLists.add(filterdByMinVideos);
        }
        if(maxVideos!=null){
            int iMaxVideos = Integer.parseInt(maxVideos);
            filtredByMaxVideos = filter.getReportsWithMaximumVideosAmount(iMaxVideos);
            filtredLists.add(filtredByMaxVideos);
        }


        if(!location.equals(DataForFilterActivityExtractor.REPORT_LOCATION_NOT_CHOOSEN)){
            filtredByLocation = filter.getReportsByReportLocalization(location);
            filtredLists.add(filtredByLocation);
        }
        if(!category.equals(DataForFilterActivityExtractor.REPORT_CATEGORY_NOT_CHOOSEN)){
            filtredLists.add(filter.getReportsByReportCategory(category));
        }
        if(!epoka.equals(DataForFilterActivityExtractor.REPORT_EPOKA_NOT_CHOOSEN)){
            filtredLists.add(filter.getReportsByReportEpoka(epoka));
        }
        if(!cloth.equals(DataForFilterActivityExtractor.REPORT_CLOTH_NOT_CHOOSEN)){
            filtredLists.add(filter.getReportsByReportCloth(cloth));
        }
        if(!weapon.equals(DataForFilterActivityExtractor.REPORT_WEAPON_NOT_CHOOSEN)){
            filtredLists.add(filter.getReportsByReportWeapon(weapon));
        }
        if(!accessory.equals(DataForFilterActivityExtractor.REPORT_ACCESSORY_NOT_CHOOSEN)){
            filtredLists.add(filter.getReportsByReportAccessory(accessory));
        }
        if(!vehicle.equals(DataForFilterActivityExtractor.REPORT_VEHICLE_NOT_CHOOSEN)){
            filtredLists.add(filter.getReportsByReportVehicle(vehicle));
        }

        if (!rbDate.isChecked()) {
            if (!month.equals(DataForFilterActivityExtractor.REPORT_MONTH_NOT_CHOOSEN)) {
                int iYear = Integer.parseInt(year);
                int iMonth = Integer.parseInt(month);
                filtredByMonthAndYear = filter.getReportsFromSelectedYearAndMonth(iYear, iMonth);
                filtredLists.add(filtredByMonthAndYear);
            }
            if (!year.equals(DataForFilterActivityExtractor.REPORT_YEAR_NOT_CHOOSEN)) {
                int iYear = Integer.parseInt(year);
                filtredByYear = filter.getReportsFromSelectedYear(iYear);
                filtredLists.add(filtredByYear);
            }
        }
        else {
            if (!date.equals(DataForFilterActivityExtractor.REPORT_DATE_NOT_CHOOSEN)) {
                filtredByDate = filterByPickedDate();
                filtredLists.add(filtredByDate);
            }
        }

        result = filter.intersectResults(filtredLists);

        return manageFilterResult(filtredLists, result);
    }

    private String getTvMaxVideosText() {
        return getTvText(maxVideoBar);
    }


    private List<ReportEntity> manageFilterResult(List<List<ReportEntity>> filtredLists, List<ReportEntity> result) {
        if (!filtredLists.isEmpty() && result.isEmpty()){
            Log.e(TAG, "Filter result is empty");
            return Filter.LIST_EMPTY_RESULT;
        }
        if(filtredLists.isEmpty()){
            Log.e(TAG, "No filter activated");
            return filter.getAllReports();
        }
        return result;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case  R.id.filterRBPickDate:
                turnPickDateLayoutOn();
                break;
            case R.id.filterRBPickCustomDate:
                turnPickCustomDateLayoutOn();
                break;
            default:
                break;
        }
    }

    private void turnPickCustomDateLayoutOn() {
        llMonthAndYear.setVisibility(View.VISIBLE);
        sDate.setVisibility(View.GONE);
    }

    private void turnPickDateLayoutOn() {
        llMonthAndYear.setVisibility(View.GONE);
        sDate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "Spinner on item selected trigger");
        if (parent.getId() == R.id.filterSPickYear) {
            Log.e(TAG, "Selected item: " + parent.getSelectedItem());
            updateMonthSpinnerData(parent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateMonthSpinnerData(AdapterView<?> parent) {
        changeMonthSpinnerDataBySelectedYear(parent.getSelectedItem().toString());
    }

    private List<ReportEntity> filterByPickedDate() {
        String pickedDate = sDate.getSelectedItem().toString();
        Log.e(TAG, "Date Spinner selected: " + pickedDate);
        int selectedItemPosition = sDate.getSelectedItemPosition();
        Log.e(TAG, "Data Spinner selected position " + selectedItemPosition);
        switch (pickedDate){
            case DataForFilterActivityExtractor.TIME_PREVIOUS_WEEK:
                 return filter.getReportsFromPastDays(7);
            case DataForFilterActivityExtractor.TIME_PREVIOUS_MONTH:
                return filter.getReportsFromPastMonths(1);
            case DataForFilterActivityExtractor.TIME_PREVIOUS_THREE_MONTHS:
                return filter.getReportsFromPastMonths(3);
            case DataForFilterActivityExtractor.TIME_PREVIOUS_YEAR:
                return filter.getReportsFromPastYears(1);
            default:
                return null;
        }
    }

}
