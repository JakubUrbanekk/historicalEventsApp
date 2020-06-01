package com.example.inzynierka;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inzynierka.Report.AddReport.AddReportActivity;
import com.example.inzynierka.Report.ListOfReports.ListOfReportsActivity;
import com.example.inzynierka.equipments.MainEquipmentsActivity;
import com.example.inzynierka.informations.listOfInformations.InformationsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

// import com.example.inzynierka.EventCreation.EventCreationActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button report;
    Button viewReports;
    Button searchButton;
    Button equipmentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                101);
    }
    private void initButtons(){
        report =(Button)findViewById(R.id.addReportButton);
        viewReports = (Button) findViewById(R.id.viewReportsButton);
        searchButton = (Button) findViewById(R.id.searchInformationButton);
        equipmentButton = (Button) findViewById(R.id.equipmentButton);

        equipmentButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainEquipmentsActivity.class);
            MainActivity.this.startActivity(intent);
        });

        report.setOnClickListener(this);
        viewReports.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }

    public void reportListener(View view) {
        Intent intent = new Intent(MainActivity.this, AddReportActivity.class);
        MainActivity.this.startActivity(intent);
    }
    public void viewReportListener(View view){
        Intent intent = new Intent(MainActivity.this, ListOfReportsActivity.class);
        MainActivity.this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addReportButton:
                reportListener(v);
                break;
            case R.id.viewReportsButton:
                viewReportListener(v);
                break;
            case R.id.searchInformationButton:
                openInformationsActivity();

        }
    }

    private void openInformationsActivity() {
        Intent intent = new Intent(MainActivity.this, InformationsActivity.class);
        startActivity(intent);
    }
}
