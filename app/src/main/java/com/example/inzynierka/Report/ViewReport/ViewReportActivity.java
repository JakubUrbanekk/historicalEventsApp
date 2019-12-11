package com.example.inzynierka.Report.ViewReport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.example.inzynierka.R;
import com.example.inzynierka.Report.AddReport.AddReportViewModel;

public class ViewReportActivity extends AppCompatActivity {
    ViewReportModel viewReportModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Intent intent = getIntent();
        viewReportModel = ViewModelProviders.of(this).get(ViewReportModel.class);
      //  viewReportModel.setCurrentReport(viewReportModel.getReportById(intent.getIntExtra("currentReport",0)));
    }
}
