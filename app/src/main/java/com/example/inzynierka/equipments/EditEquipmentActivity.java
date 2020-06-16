package com.example.inzynierka.equipments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Adapters.EquipmentAdapter;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.FileManager;
import com.google.android.material.textfield.TextInputEditText;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
public class EditEquipmentActivity extends EditableEquipmentActivity {
    IEquipment iEquipment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long id = intent.getLongExtra(EquipmentAdapter.BUNDLE_EQUIPMENT_ID, -1);
        new GetEquipmentRequestTask(id).execute();
    }

    private void initValues(IEquipment equipment) {
        setTextToEditText(editTextDescription,equipment.getDescription());
        setTextToEditText(editTextTitle, equipment.getName());
        spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition(equipment.getCategory()));

        Glide
                .with(this)
                .load(equipment.getPhotoUri())
                .placeholder(R.drawable.noimge)
                .into(imageView);

    }

    private void setTextToEditText(TextInputEditText editText, String text) {
        editText.setText(text);
    }

    @Override
    protected void addToDatabase() {
        String photoUriToString = "";
        if (photoUri != null) {
            FileManager fileManager = new FileManager(this);
            photoUri = fileManager.saveImage(photoUri);
            photoUriToString = photoUri.toString();
        }
        iEquipment.setName(getStringFromEditText(editTextTitle));
        iEquipment.setDescription(getStringFromEditText(editTextDescription));
        iEquipment.setPhotoUri(photoUriToString);
        String category = spinner.getSelectedItem().toString();
        if(category.equals("Dodaj nowy")){
            category = "Nieokreślony";
        }
        iEquipment.setCategory(category);

        new UpdateEquipmentRequestTask(iEquipment, this).execute();
    }

    @Override
    public void setTextViewNOEQ(String text) {

    }

    @AllArgsConstructor
    public class GetEquipmentRequestTask extends AsyncTask<Void, Void, IEquipment> {
        long id;
        @Override
        protected IEquipment doInBackground(Void... voids) {
            return repository.get(id);
        }

        @Override
        protected void onPostExecute(IEquipment equipment) {
            iEquipment = equipment;
            initValues(equipment);
        }
    }
    @AllArgsConstructor
    public class UpdateEquipmentRequestTask extends AsyncTask<Void, Void, Void> {
        IEquipment iEquipment;
        Activity activity;
        @Override
        protected Void doInBackground(Void... voids) {
            log.info("Equipment " + iEquipment);
            repository.update(iEquipment);
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
