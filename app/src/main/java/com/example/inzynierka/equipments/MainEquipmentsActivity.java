package com.example.inzynierka.equipments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.inzynierka.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainEquipmentsActivity extends AppCompatActivity {
    private ImageButton clothsButton;
    private ImageButton weaponsButton;
    private ImageButton vehicleButton;
    private ImageButton accessoriessButton;
    public static final String BUNDLE_EQUIPMENT_TYPE = "equipmentType";
    public static final int BUNDLE_EQUIPMENT_CLOTH = 1;
    public static final int BUNDLE_EQUIPMENT_WEAPON = 2;
    public static final int BUNDLE_EQUIPMENT_VEHICLE= 3;
    public static final int BUNDLE_EQUIPMENT_ACCESSORIES = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_equipments);
        initView();
    }
    private void initView(){
        clothsButton = (ImageButton) findViewById(R.id.mainEquipmentClothButton);
        clothsButton.setOnClickListener(view -> startActivityWithBundle(BUNDLE_EQUIPMENT_TYPE, BUNDLE_EQUIPMENT_CLOTH));
        weaponsButton = (ImageButton) findViewById(R.id.mainEquipmentWeaponsButton);
        weaponsButton.setOnClickListener(view -> startActivityWithBundle(BUNDLE_EQUIPMENT_TYPE, BUNDLE_EQUIPMENT_WEAPON));
        vehicleButton = (ImageButton) findViewById(R.id.mainEquipmentVehicleButton);
        vehicleButton.setOnClickListener(view -> startActivityWithBundle(BUNDLE_EQUIPMENT_TYPE, BUNDLE_EQUIPMENT_VEHICLE));
        accessoriessButton = (ImageButton) findViewById(R.id.mainEquipmentAccesorriesButton);
        accessoriessButton.setOnClickListener(view -> startActivityWithBundle(BUNDLE_EQUIPMENT_TYPE, BUNDLE_EQUIPMENT_ACCESSORIES));

    }

    private void startActivityWithBundle(String key, int value){
        Intent intent = new Intent(this, EquipmentListActivity.class);
        intent.putExtra(key, value);
        startActivity(intent);
    }
}
