package com.example.inzynierka;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inzynierka.EventCreation.EventCreationActivity;
import com.example.inzynierka.Report.ReportActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {
    Button report;
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
        report =(Button)findViewById(R.id.report);
    }

    public void reportListener(View view) {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void eventCreationListener(View view) {
        Intent intent = new Intent(MainActivity.this, EventCreationActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
