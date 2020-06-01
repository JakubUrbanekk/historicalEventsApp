package com.example.inzynierka.equipments;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Database.equipment.IEquipmentRepository;
import com.example.inzynierka.Database.equipment.accessories.AccessoriesRepository;
import com.example.inzynierka.Database.equipment.clothes.ClothRepository;
import com.example.inzynierka.Database.equipment.vehicles.VehicleRepository;
import com.example.inzynierka.Database.equipment.weapons.WeaponRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import lombok.extern.java.Log;

@Log
public abstract class EquipmentBundleActivity extends AppCompatActivity {
    IEquipmentRepository repository;
    protected int bundleValue;
    public abstract void setTitleTextViewText(String title);

    protected void initRepository(){
        int value = getExtraBundle();
        bundleValue = value;
        log.info("Value from bundle " + value);
        switch (value) {
            case MainEquipmentsActivity.BUNDLE_EQUIPMENT_CLOTH:
                repository = new ClothRepository(getApplication());
                log.info("Repository created");
                setTitleTextViewText("Stroje");
                break;
            case MainEquipmentsActivity.BUNDLE_EQUIPMENT_WEAPON:
                repository = new WeaponRepository(getApplication());
                log.info("Repository created");
                setTitleTextViewText("Bronie");
                break;
            case MainEquipmentsActivity.BUNDLE_EQUIPMENT_VEHICLE:
                repository = new VehicleRepository(getApplication());
                log.info("Repository created");
                setTitleTextViewText("Pojazdy");
                break;
            case MainEquipmentsActivity.BUNDLE_EQUIPMENT_ACCESSORIES:
                repository = new AccessoriesRepository(getApplication());
                log.info("Repository created");
                setTitleTextViewText("Akcesoria");
                break;
            default:
                break;
        }
        new GetEquipmentRequestTask().execute();
    }

    private int getExtraBundle(){
        String key = MainEquipmentsActivity.BUNDLE_EQUIPMENT_TYPE;
        Intent intent = getIntent();
        int value = intent.getIntExtra(key, -1);
        return value;
    }

    public void putEquipmentExtra(Intent intent){
        intent.putExtra(MainEquipmentsActivity.BUNDLE_EQUIPMENT_TYPE, getExtraBundle());
    }

    public class GetEquipmentRequestTask extends AsyncTask<Void, Void, List<IEquipment>> {

        @Override
        protected List<IEquipment> doInBackground(Void... voids) {
            return repository.getAll();
        }

        @Override
        protected void onPostExecute(List<IEquipment> iEquipments) {
            List<String> list = iEquipments.stream()
                    .map(IEquipment::getCategory)
                    .collect(Collectors.toList());
            Set<String> set = new HashSet<>(list);

            initSpinner(set);
        }
    }

    protected abstract void initSpinner(Set<String> set);
}
