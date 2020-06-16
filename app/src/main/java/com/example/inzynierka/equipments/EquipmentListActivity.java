package com.example.inzynierka.equipments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.inzynierka.Adapters.EquipmentAdapter;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Database.Report.ReportRepository;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.R;
import com.example.inzynierka.addons.FinalVariables;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
public class EquipmentListActivity extends EquipmentBundleActivity {
    private RecyclerView listRecyclerView;
    private TextView textViewTitle;
    private TextView textViewNoEq;
    private ReportRepository reportRepository;
    private EquipmentAdapter equipmentAdapter;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);
        reportRepository = new ReportRepository(getApplication());
        initView();
        initRepository();
        new GetEquipmentRequestTask().execute();
        new GetReportsRequestTask().execute();
    }

    private void initView() {
        listRecyclerView = (RecyclerView) findViewById(R.id.equipmentListRecyclerView);
        textViewTitle = (TextView) findViewById(R.id.equipmentListTextViewTitle);
        textViewNoEq = (TextView) findViewById(R.id.equipmentListNoEQTextView);
        fab = (FloatingActionButton) findViewById(R.id.addEquipmentFloatingButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddEquipmentActivity.class);
            putEquipmentExtra(intent);
            startActivity(intent);
        });
    }

    @Override
    public void setTitleTextViewText(String s) {
        textViewTitle.setText(s);
    }

    @Override
    public void setTextViewNOEQ(String s) {
        textViewNoEq.setText(s);
    }

    @Override
    protected void initSpinner(Set<String> set) {
    }


    private void initRecyclerViewAdapter(List<IEquipment> list){
        if(list.isEmpty()){
            textViewNoEq.setVisibility(View.VISIBLE);
        }
        else {
            textViewNoEq.setVisibility(View.GONE);
        }

        equipmentAdapter = new EquipmentAdapter(this, list, repository);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        listRecyclerView.setLayoutManager(mLayoutManager);
        listRecyclerView.setAdapter(equipmentAdapter);
    }

    public class GetEquipmentRequestTask extends AsyncTask<Void, Void, List<IEquipment>> {

        @Override
        protected List<IEquipment> doInBackground(Void... voids) {
            return repository.getAll();
        }

        @Override
        protected void onPostExecute(List<IEquipment> iEquipments) {
            initRecyclerViewAdapter(iEquipments);
        }
    }

    public class GetReportsRequestTask extends AsyncTask<Void, Void, List<ReportEntity>>{

        @Override
        protected List<ReportEntity> doInBackground(Void... voids) {
            return reportRepository.getAll();
        }
        @Override
        protected void onPostExecute(List<ReportEntity> list) {
            log.info ("Budnle " + bundleValue + " Report List " + list);
            switch (bundleValue){
                case MainEquipmentsActivity.BUNDLE_EQUIPMENT_CLOTH:
                    List<String> mappedToString = list.stream()
                            .filter(reportEntity ->  !reportEntity.getVehicle().equals(FinalVariables.CLOTH_NOT_SELECTED_CONST))
                            .map(report ->report.getCloth())
                            .collect(Collectors.toList());
                    log.info("Cloth from report " + mappedToString);
                    Set<String> set = new HashSet<>(mappedToString);
                    new AddEquipmentRequestTask(set).execute();
                    break;
                case MainEquipmentsActivity.BUNDLE_EQUIPMENT_WEAPON:
                    List<String> mappedToString2 = list.stream()
                            .filter(reportEntity ->  !reportEntity.getVehicle().equals(FinalVariables.WEAPON_NOT_SELECTED_CONST))
                            .map(report ->report.getWeapon())
                            .collect(Collectors.toList());
                    Set<String> set2 = new HashSet<>(mappedToString2);
                    new AddEquipmentRequestTask(set2).execute();
                    break;
                case MainEquipmentsActivity.BUNDLE_EQUIPMENT_VEHICLE:
                    log.info("Vehicle list");
                    List<String> mappedToString3 = list.stream()
                            .filter(reportEntity ->  !reportEntity.getVehicle().equals(FinalVariables.VEHICLE_NOT_SELECTED_CONST))
                            .map(report ->report.getVehicle())
                            .collect(Collectors.toList());
                    Set<String> set3 = new HashSet<>(mappedToString3);
                    new AddEquipmentRequestTask(set3).execute();
                    break;
                case MainEquipmentsActivity.BUNDLE_EQUIPMENT_ACCESSORIES:
                    List<String> mappedToString4 = list.stream()
                            .filter(reportEntity ->  !reportEntity.getVehicle().equals(FinalVariables.ACCESSORY_NOT_SELECTED_CONST))
                            .map(report ->report.getAccessory())
                            .collect(Collectors.toList());
                    Set<String> set4 = new HashSet<>(mappedToString4);
                    new AddEquipmentRequestTask(set4).execute();
                    break;
                default:
                    break;
            }
        }
    }

    @AllArgsConstructor
    public class AddEquipmentRequestTask extends AsyncTask<Void, Void, List<IEquipment>> {
        Set<String> set;
        @Override
        protected List<IEquipment> doInBackground(Void... voids) {
            List<IEquipment> getAll = repository.getAll();
            List<String> allNames = getAll.stream()
                    .map(iequipment-> iequipment.getName())
                    .collect(Collectors.toList());

            Set<String> ditinctNames = new HashSet<>(allNames);
            log.info("Distinct names " + ditinctNames);
            log.info ("All cloth from report "  + set);
            for(String string:set){
                if(!ditinctNames.contains(string)){
                    IEquipment iEquipment = new IEquipment(string);
                    repository.insert(iEquipment);
                }
            }

            return repository.getAll();
        }

        @Override
        protected void onPostExecute(List<IEquipment> iEquipments) {
            equipmentAdapter.setEquipmentList(iEquipments);
        }
    }

}
