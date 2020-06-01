package com.example.inzynierka.equipments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.ActionBottomDialogFragment;
import com.example.inzynierka.addons.FinalVariables;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import lombok.extern.java.Log;

@Log
public abstract class EditableEquipmentActivity extends EquipmentBundleActivity implements ActionBottomDialogFragment.ItemClickListener {
    protected TextView titleTextView;
    protected Button addPhotoButton;
    protected FloatingActionButton fab;
    protected Spinner spinner;
    protected ImageView imageView;
    protected TextInputEditText editTextTitle;
    protected TextInputEditText editTextDescription;
    protected Uri photoUri;
    protected final String PHOTO_GALLERY = "Dodaj zdjęcie z galerii";
    protected final String PHOTO_CAMERA = "Zrób zdjęcie";
    protected final int GALLERY_REQUEST_CODE = 1;
    protected final int CAMERA_REQUEST_CODE = 2;
    private final String ADD_NEW_SPINNER_OPTION = "Dodaj nowy";
    private final String SPINNER_DEFAULT = "Nieokreślony";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);
        initView();
        initRepository();
    }

    private void initView() {
        titleTextView = (TextView) findViewById(R.id.addEquipmentTitleTextView);
        addPhotoButton = (Button) findViewById(R.id.addEquipmentAddPhotoButton);
        editTextTitle = (TextInputEditText) findViewById(R.id.addEquipmentTitleEditText);
        editTextDescription = (TextInputEditText) findViewById(R.id.addEquipmentDescriptionEditText);
        addPhotoButton.setOnClickListener(view -> openBottomDialog());
        fab = (FloatingActionButton) findViewById(R.id.addEquipmentFloatingButton);
        fab.setOnClickListener(view -> addToDatabase());
        spinner = (Spinner) findViewById(R.id.addEquipmentCategorySpinner);
        imageView = (ImageView) findViewById(R.id.addEquipmentImageView);
    }

    protected abstract void addToDatabase();

    protected String getStringFromEditText(TextInputEditText editTextTitle) {
        if(editTextTitle.getText() != null && !StringUtils.isEmpty(editTextTitle.getText())){
            return editTextTitle.getText().toString();
        }
        return "Brak";
    }


    private void openBottomDialog() {
        ActionBottomDialogFragment actionBottomDialogFragment = new ActionBottomDialogFragment(PHOTO_GALLERY, PHOTO_CAMERA);
        actionBottomDialogFragment.show(getSupportFragmentManager(),
                "Asd");
    }

    @Override
    public void onItemClick(View item) {
        switch (item.getId()) {
            case R.id.barBottomTvGallery:
                openCamera(CAMERA_REQUEST_CODE);
                break;
            case R.id.barBottomTvCamera:
                openGallery(GALLERY_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void loadImage() {
        Glide
                .with(getApplicationContext())
                .load(photoUri)
                .into(imageView);
    }

    private void openGallery(int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        } else {
            startActivityForResult(photoPickerIntent, requestCode);
        }
    }

    public void openCamera(int requestCode) {
        Intent cameraIntent;
        cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, requestCode);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        log.info("Result code " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    final Uri uri = data.getData();
                    log.info("Image uri " + uri);
                    photoUri = uri;
                    loadImage();
                    break;
                case CAMERA_REQUEST_CODE:
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    if (image != null) {
                        Uri imageUri = getImageUri(this, image);
                        log.info("Image uri " + imageUri);
                        photoUri = imageUri;
                        loadImage();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void setTitleTextViewText(String s) {
        titleTextView.setText(s);
    }

    @Override
    protected void initSpinner(Set<String> equipments) {
        Set <String> spinnerArray = getSpinnerOptionsList(equipments);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_cheked_item_small,
                new ArrayList<>(spinnerArray));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
//       addTextToSpinner(spinnerAdapter, SPINNER_DEFAULT, 0);
//        log.info("Equipments " + equipments);
//        equipments.stream()
//                .forEach(equipment ->
//                        addTextToSpinner(spinnerAdapter, equipment, spinnerAdapter.getCount()));
//
//        addTextToSpinner(spinnerAdapter, ADD_NEW_SPINNER_OPTION, spinnerAdapter.getCount());
        addDefaultListenerToSpinner(spinner, spinnerAdapter.getCount());
    }

    protected Set<String> getSpinnerOptionsList(Set<String> equipments){
        Set <String> spinnerArray = new LinkedHashSet<>();
        spinnerArray.add(SPINNER_DEFAULT);
        spinnerArray.addAll(equipments);
        spinnerArray.add(ADD_NEW_SPINNER_OPTION);
        return spinnerArray;
    }


    private void addTextToSpinner(ArrayAdapter<String> adapter, String text, int position){
        if(text == null){
            return;
        }

        log.info("Adding text to spinner " + text);
        List<String> spinnerOptions = getSpinnerOptions(adapter);
        log.info("Spinner options " + spinnerOptions);
        spinnerOptions = spinnerOptions.stream()
        .map(String::toLowerCase)
                .collect(Collectors.toList());

        String lowerText = text.toLowerCase();

        if(!spinnerOptions.contains(lowerText)) {
            log.info("Text added");
            text = StringUtils.capitalize(text);
            adapter.insert(text,position);
            adapter.notifyDataSetChanged();
        }

        log.info("Spinner options after insert" + getSpinnerOptions(adapter));
    }

    private List<String> getSpinnerOptions(ArrayAdapter<String> adapter) {
        int size = adapter.getCount();
        List<String> arrayList = new ArrayList<>();
        for (int i=0 ;i<size-1; i++){
            arrayList.add(adapter.getItem(i).toLowerCase());
        }
        return arrayList;
    }

    private void addDefaultListenerToSpinner(Spinner spinner, int count){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().toString().equals(FinalVariables.CATEGORY_ADD_NEW)){
                    createDialogBoxWithEditText(spinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createDialogBoxWithEditText(Spinner spinner){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("Dodaj nowy rodzaj");
        alert.setView(edittext);
        alert.setPositiveButton("Dodaj", (dialog, whichButton) -> {
            //What ever you want to do with the value
            //OR
            String editTextValue = edittext.getText().toString();
            addTextToSpinner((ArrayAdapter<String>) spinner.getAdapter(), editTextValue, spinner.getCount() -1);
            spinner.setSelection(spinner.getAdapter().getCount()-2);
        });
        alert.setNegativeButton("Anuluj", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });
        alert.show();
    }
}

