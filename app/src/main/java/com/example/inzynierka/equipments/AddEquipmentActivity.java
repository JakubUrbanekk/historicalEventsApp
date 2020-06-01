package com.example.inzynierka.equipments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Report.FileManager;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
public class AddEquipmentActivity extends EditableEquipmentActivity{

    protected void addToDatabase() {
        String photoUriToString = "";
        if (photoUri != null) {
            FileManager fileManager = new FileManager(this);
            photoUri = fileManager.saveImage(photoUri);
            photoUriToString = photoUri.toString();
        }
        String title = getStringFromEditText(editTextTitle);
        String description = getStringFromEditText(editTextDescription);
        String category = spinner.getSelectedItem().toString();
        if(category.equals("Dodaj nowy")){
            category = "Nieokreślony";
        }
        IEquipment equipment = new IEquipment(title,category,photoUriToString, description);
        new AddEquipmentRequestTask(equipment, this).execute();
    }

    @AllArgsConstructor
    public class AddEquipmentRequestTask extends AsyncTask<Void, Void, Void> {
        IEquipment iEquipment;
        Activity activity;
        @Override
        protected Void doInBackground(Void... voids) {
            log.info("Equipment " + iEquipment);
            repository.insert(iEquipment);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(activity, EquipmentListActivity.class);
            putEquipmentExtra(intent);
            activity.startActivity(intent);
        }
    }
}

