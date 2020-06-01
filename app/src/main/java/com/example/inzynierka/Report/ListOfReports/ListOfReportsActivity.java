package com.example.inzynierka.Report.ListOfReports;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.MainActivity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.AddReport.AddReportActivity;
import com.example.inzynierka.Report.ListOfReports.Filter.FilterActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfReportsActivity extends AppCompatActivity implements View.OnClickListener{
    static final int FILTER_REQUEST_CODE = 1;
    RecyclerView recyclerViewReportsList;
    private static ReportListAdapter reportListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListOfReportsViewModel viewModel;
    private Button sortButton;
    private Button filtrButton;
    private FloatingActionButton addNewReportButton;
    private LinearLayout noImageLayout;
    private LinearLayout recyclerViewLayout;
    private TextView tvFiltrActivated;
    private TextView tvNoReports;
    final String TAG = "Reports List Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_reports);
        viewModel = ViewModelProviders.of(this).get(ListOfReportsViewModel.class);
        initView();
        initObservators();

    }
    void initObservators(){
        initAllReportsObservator();
        initCurrentReportsObservator();
    }

    private void initCurrentReportsObservator() {
        viewModel.getListCurrentReports().observe(this, new Observer<List<ReportEntity>>() {
            @Override
            public void onChanged(List<ReportEntity> reportEntityList) {
                    if(reportEntityList.size()<viewModel.getAllReportsSize()) {
                    showFilterActivatedTextView();
                }
                    else {
                        disableFilterActivatedTextView();
                    }
                manageViewDependsOnReports(reportEntityList);
                reportListAdapter.setReports(reportEntityList);
            }
        });
    }

    private void disableFilterActivatedTextView() {
        filtrButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_list_black_24dp, 0, 0, 0);
    }

    private void showFilterActivatedTextView() {
        filtrButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_list_green_24dp, 0, 0, 0);
    }

    private void initAllReportsObservator(){
        viewModel.getAllReports().observe(this, new Observer<List<ReportEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ReportEntity> reportEntityList) {
                // Update the cached copy of the words in the adapter.
                viewModel.setListAllReports(reportEntityList);
                viewModel.sortResults(viewModel.getCurrentOrder());
                reportListAdapter.setReports(viewModel.getListAllReports());
                Log.e("Obserwator Lista", viewModel.getListAllReports().size()+"");
                manageViewDependsOnReports(viewModel.getListAllReports());
                for (ReportEntity reportEntity: reportEntityList){
                    Log.i("ReportEntityList", reportEntity+"");
                }
            }
        });
    }

    private void manageViewDependsOnReports(List<ReportEntity> reports){
        if(reports.size()==0){
            hideView();
        }
        else {
            showView();
        }
    }

    private void hideView(){
        recyclerViewLayout.setVisibility(View.GONE);
        noImageLayout.setVisibility(View.VISIBLE);
        String text;
        if(viewModel.isFilterResultEmpty()){
            text = getString(R.string.filter_empty_result);
        }
        else
        {
            text = getString(R.string.add_first_report);
        }
        Log.e(TAG, "Text for no reports " + text);
        tvNoReports.setText(text);
    }

    private void showView(){
        recyclerViewLayout.setVisibility(View.VISIBLE);
        noImageLayout.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.listOfReportsSortButton:
            createSortPopupMenu();
            break;
        case R.id.addReportFloatingButton:
            openCreateViewActivity();
            break;
        case R.id.listOfReportsFiltrButton:
             openFilterActivity();
              break;
     }
    }

    private void openFilterActivity() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, FILTER_REQUEST_CODE);
    }

    private void openCreateViewActivity() {
        Intent intent = new Intent(this, AddReportActivity.class);
        startActivity(intent);
    }

    private void initView(){
        tvNoReports = (TextView) findViewById(R.id.listOfReportsNoReportTextView);
        tvFiltrActivated = (TextView) findViewById(R.id.reportsListTVFilterActivated);
        recyclerViewLayout = (LinearLayout) findViewById(R.id.listOfReportsRecyclerViewLayout);
        noImageLayout = (LinearLayout) findViewById(R.id.listOfReportsNoImageLayout);
        recyclerViewReportsList = (RecyclerView) findViewById(R.id.listReportRecyclerView);
        sortButton = (Button) findViewById(R.id.listOfReportsSortButton);
        sortButton.setOnClickListener(this);
        filtrButton = (Button) findViewById(R.id.listOfReportsFiltrButton);
        filtrButton.setOnClickListener(this);
        addNewReportButton = (FloatingActionButton) findViewById(R.id.addReportFloatingButton);
        addNewReportButton.setOnClickListener(this);
        recyclerViewReportsList.setHasFixedSize(true);
        recyclerViewReportsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addNewReportButton.getVisibility() == View.VISIBLE) {
                    addNewReportButton.hide();
                } else if (dy < 0 && addNewReportButton.getVisibility() != View.VISIBLE) {
                    addNewReportButton.show();
                }
            }
        });
        setupAdapter();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setupAdapter(){
        layoutManager = new LinearLayoutManager(this);
        recyclerViewReportsList.setLayoutManager(layoutManager);
        recyclerViewReportsList.setItemAnimator(new DefaultItemAnimator());
        reportListAdapter = new ReportListAdapter(this, viewModel);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        }
        else {
            recyclerViewReportsList.setAdapter(reportListAdapter);
        }
    }

    private void createSortPopupMenu() {
        final PopupMenu popup = new PopupMenu(this, sortButton);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.sort_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.byDate:
                        sortByDate();
                        break;
                    case R.id.byLocalization:
                        sortByLocalization();
                        break;
                    case R.id.byTitle:
                        sortByTitle();
                }
            return true;
            }

            private void sortByLocalization() {
                viewModel.sortResults(viewModel.BY_LOCALIZATION);
                reportListAdapter.setReports(viewModel.getListAllReports());
            }

            private void sortByTitle() {
                viewModel.sortResults(viewModel.BY_TITLE);
                reportListAdapter.setReports(viewModel.getListAllReports());
            }

            private void sortByDate() {
                viewModel.sortResults(viewModel.BY_DATE);
                reportListAdapter.setReports(viewModel.getListAllReports());
            }
        });
        popup.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Request Code "+ requestCode);
        if(requestCode == FILTER_REQUEST_CODE){
            if(data!=null) {
                ArrayList<ReportEntity> filterResult = data.getParcelableArrayListExtra(FilterActivity.STRING_FILTER_RESULT);
                if(data.getStringExtra(FilterActivity.STRING_FILTER_EMPTY_RESULT)!=null){
                    Log.e(TAG, "filter return empty result");
                    viewModel.setFilterResultEmpty(true);
                }
                else {
                    Log.e(TAG, "filter return non empty result");
                    viewModel.setFilterResultEmpty(false);
                }
                filterResult.stream()
                        .forEach(reportEntity -> Log.e(TAG, "report from filter activity" + reportEntity.toString()));

                viewModel.setListCurrentReports(filterResult);
            }
        }
    }
}
