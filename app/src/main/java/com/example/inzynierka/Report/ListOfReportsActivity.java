package com.example.inzynierka.Report;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;

import java.util.List;

public class ListOfReportsActivity extends AppCompatActivity {
    RecyclerView recyclerViewReportsList;
    private static ReportListAdapter reportListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListOfReportsViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_reports);
        viewModel = ViewModelProviders.of(this).get(ListOfReportsViewModel.class);

        recyclerViewReportsList = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerViewReportsList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerViewReportsList.setLayoutManager(layoutManager);
        recyclerViewReportsList.setItemAnimator(new DefaultItemAnimator());
        reportListAdapter = new ReportListAdapter();
        recyclerViewReportsList.setAdapter(reportListAdapter);

        viewModel.getAllReports().observe(this, new Observer<List<ReportEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ReportEntity> reportEntityList) {
                // Update the cached copy of the words in the adapter.
                reportListAdapter.setReports(reportEntityList);
            }
        });
    }
}
