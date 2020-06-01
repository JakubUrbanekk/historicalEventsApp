package com.example.inzynierka.Report.ViewReport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ViewReportActivity extends AppCompatActivity {
    TextView city;
    TextView title;
    TextView description;
    TextView date;
    ViewReportModel viewReportModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Intent intent = getIntent();
        viewReportModel = ViewModelProviders.of(this).get(ViewReportModel.class);
        int reportId = intent.getIntExtra("currentReport", 0);
        viewReportModel.getReportById(reportId).observe(this, new Observer<ReportEntity>() {
            @Override
            public void onChanged(ReportEntity reportEntity) {
                viewReportModel.setCurrentReport(reportEntity);
                intiView();
            }
        });


        //  viewReportModel.setCurrentReport(viewReportModel.getReportById(intent.getIntExtra("currentReport",0)));
    }

    private void intiView() {
        date = (TextView) findViewById(R.id.viewDate);
        city = (TextView) findViewById(R.id.viewCity);
        description = (TextView) findViewById(R.id.viewDescription);
        title = (TextView) findViewById(R.id.viewTitle);

        ReportEntity reportEntity = viewReportModel.getCurrentReport();

        title.setText(reportEntity.getReportTitle());
        date.setText(reportEntity.getReportDate());
        city.setText(reportEntity.getReportLocalization());
        description.setText(reportEntity.getReportDescription());

    }
}
