package com.example.inzynierka.Report.ListOfReports;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.inzynierka.Adapters.ReportListAdapter;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.MainActivity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.AddReport.AddReportActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListOfReportsActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerViewReportsList;
    private static ReportListAdapter reportListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListOfReportsViewModel viewModel;
    private Button sortButton;
    private Button filtrButton;
    private FloatingActionButton addNewReportButton;
    private LinearLayout noImageLayout;
    private LinearLayout recyclerViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_reports);
        setSupportActionBar((Toolbar)findViewById(R.id.listReportToolbar));
        viewModel = ViewModelProviders.of(this).get(ListOfReportsViewModel.class);
        initView();

        viewModel.getAllReports().observe(this, new Observer<List<ReportEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ReportEntity> reportEntityList) {
                // Update the cached copy of the words in the adapter.
                viewModel.setListAllReports(reportEntityList);
                viewModel.sortResults(viewModel.getCurrentOrder());
                reportListAdapter.setReports(viewModel.getListAllReports());
                Log.e("Obserwator Lista", viewModel.getListAllReports().size()+"");
                if(viewModel.getListAllReports().size()==0){
                    recyclerViewLayout.setVisibility(View.GONE);
                    noImageLayout.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerViewLayout.setVisibility(View.VISIBLE);
                    noImageLayout.setVisibility(View.GONE);
                }
                for (ReportEntity reportEntity: reportEntityList){
                    Log.i("ReportEntityList", reportEntity+"");
                }
            }
        });
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
     }
    }

    private void openCreateViewActivity() {
        Intent intent = new Intent(this, AddReportActivity.class);
        startActivity(intent);
    }

    private void initView(){
        recyclerViewLayout = (LinearLayout) findViewById(R.id.listOfReportsRecyclerViewLayout);
        noImageLayout = (LinearLayout) findViewById(R.id.listOfReportsNoImageLayout);
        recyclerViewReportsList = (RecyclerView) findViewById(R.id.listReportRecyclerView);
        sortButton = (Button) findViewById(R.id.listOfReportsSortButton);
        sortButton.setOnClickListener(this);
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
}
