package com.example.inzynierka.Report.EditReport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Adapters.EditReportPhotoAdapter;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditReportPhotoFragment extends Fragment implements View.OnClickListener {
    private static final int GALLERY_REQUEST_CODE = 1;
    static EditReportPhotoAdapter photosAdapter;
    EditReportModel viewModel;
    ReportEntity currentReport;
    RecyclerView recyclerViewPhotosList;
    ImageView mainPhoto;
    TextView textViewNoImage;
    TextInputLayout textInputLayout;
    TextInputEditText editTextPhotoDescription;
    Button addPhotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.add_photo_fragment, container, false);

        initView(rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(EditReportModel.class);
        currentReport = viewModel.getCurrentReport();
        setPhotosAdapter();

        viewModel.getPhotosCurrent().observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(List<PhotoEntity> photoEntities) {
                Log.e("Main photo", currentReport.getMainPhotoEntity().toString());
                for (PhotoEntity photoEntity : photoEntities) {
                    Log.e("Photo z rooma", photoEntity.toString());
                }
                photosAdapter.setPhotos(photoEntities);
                setMainPhoto();
            }
        });
        viewModel.getPhotosFromReport().observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(List<PhotoEntity> photoEntities) {
                viewModel.getPhotosCurrent().setValue(photoEntities);
            }
        });
        return rootView;
    }

    private void initView(View rootView) {
        recyclerViewPhotosList = (RecyclerView) rootView.findViewById(R.id.reportAddRecyclerViewPhotos);
        mainPhoto = (ImageView) rootView.findViewById(R.id.reportAddImageViewMainPhoto);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.photo_description_layout);
        addPhotos = (Button) rootView.findViewById(R.id.reportAddButtonAddPhoto);
        textViewNoImage = (TextView) rootView.findViewById(R.id.textViewNoImage);
        editTextPhotoDescription = (TextInputEditText) rootView.findViewById(R.id.addReportEditTextPhotoDescription);
        editTextPhotoDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.getMainPhotoFromCurrentReport().setPhotoDescription(editTextPhotoDescription.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        addPhotos.setOnClickListener(this);
    }
    private void setPhotosAdapter() {
        photosAdapter = new EditReportPhotoAdapter(this, currentReport, mainPhoto, editTextPhotoDescription);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPhotosList.setLayoutManager(horizontalLayoutManager);
        recyclerViewPhotosList.setAdapter(photosAdapter);

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    boolean mainPhotoSet = false;
                    if (currentReport.getMainPhoto() != null && !currentReport.getMainPhoto().toString().equals("")) {
                        mainPhotoSet = true;
                    }
                    if (data != null) {
                       /* if (data.getData() != null) {
                            Uri mImageUri = data.getData();
                            PhotoEntity photoEntity = new PhotoEntity(mImageUri);
                            photoEntity.setReportId(currentReport.getReportId());
                            viewModel.getPhotosToInsert().add(photoEntity);
                            viewModel.addToCurrentPhotos(photoEntity);
                            if (!mainPhotoSet) {
                                currentReport.setMainPhoto(photoEntity);
                            }
                        } else { */
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                PhotoEntity photoEntity = new PhotoEntity(uri);
                                Log.e("EditPhoto", " zdjecie " + photoEntity);
                                viewModel.getPhotosToInsert().add(photoEntity);
                                photoEntity.setReportId(currentReport.getReportId());
                                viewModel.addToCurrentPhotos(photoEntity);
                                if (!mainPhotoSet) {
                                    currentReport.setMainPhoto(photoEntity);
                                }
                            }
                    } else {
                    }
                        Log.e("Zmiana", "zmieniam widocznosc layoutow");
                    textViewNoImage.setVisibility(View.GONE);
                    textInputLayout.setVisibility(View.VISIBLE);
                    Log.e("Dodawanie zdjec", currentReport.toString());
                    Glide
                            .with(this)
                            .load(currentReport.getMainPhoto())
                            .placeholder(R.drawable.noimge)
                            .centerCrop()
                            .into(mainPhoto);
                    break;
                 }
            }
    }
    private void setMainPhoto() {
        if (!currentReport.getMainPhoto().toString().equals("")) {
            textViewNoImage.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.VISIBLE);
            Glide
                    .with(this)
                    .load(currentReport.getMainPhoto())
                    .centerCrop()
                    .into(mainPhoto);
            registerForContextMenu(mainPhoto);
        }
        else {
            textViewNoImage.setVisibility(View.VISIBLE);
            textInputLayout.setVisibility(View.GONE);
            Glide
                    .with(this)
                    .load(R.drawable.noimge)
                    .centerCrop()
                    .into(mainPhoto);
        }
        editTextPhotoDescription.setText(viewModel.getMainPhotoDescription());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reportAddButtonAddPhoto:
                addPhotos();
                break;
        }
    }
    private void addPhotos() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        } else {
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
        menu.setHeaderTitle("Wybierz opcję");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.deletePhoto:
                deletePhoto();
                break;
            case R.id.editPhotoDescription:
                break;

        }
        return true;
    }

    private void deletePhoto() {
        PhotoEntity photoEntity = currentReport.getMainPhotoEntity();
        viewModel.deleteFromCurrentPhotos(photoEntity);

        if (viewModel.currentPhotosSize() == 0) {
            viewModel.getMainPhotoFromCurrentReport().setPhotoUri("");
            setMainPhoto();
        }
        else if(currentReport.getMainPhotoEntity().getPhotoId()==null){
            Log.e("Bez klikniecia", "Dziala cos");
            currentReport.setMainPhoto(viewModel.firstPhotoFromList());
            setMainPhoto();
        }
        else {
            Log.e("Z kliknieciem", "Dziala cos");
            viewModel.getPhotosToDelete().add(photoEntity);
            currentReport.setMainPhoto(viewModel.firstPhotoFromList());
            setMainPhoto();
        }
    }
}

