package com.example.inzynierka.Report.AddReport;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Adapters.PhotosAdapter;
import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddPhotoFragment extends Fragment implements View.OnClickListener {
    AddReportViewModel addReportViewModel;
    RecyclerView recyclerViewPhotosList;
    Button addPhotos;
    private static final int GALLERY_REQUEST_CODE = 1;
    ImageView mainPhoto;
    TextInputLayout textInputLayout;
    static PhotosAdapter photosAdapter;
    TextView textViewNoImage;
    TextInputEditText editTextPhotoDescription;
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.add_photo_fragment, container, false);


            addReportViewModel = ViewModelProviders.of(getActivity()).get(AddReportViewModel.class);
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
                    if(!charSequence.equals("") && addReportViewModel.getMainPhotoEntity()!=null)
                    addReportViewModel.getMainPhotoEntity().setPhotoDescription(editTextPhotoDescription.getText().toString());
                    Log.i("Main photo", addReportViewModel.getMainPhotoEntity()+"");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            addPhotos.setOnClickListener(this);

            addReportViewModel.getLiveDataPhotosList().observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(@Nullable final List<PhotoEntity> photosEntites) {
                // Update the cached copy of the words in the adapter.
                Log.i("Obserwator", "Dziala");
                photosAdapter.setPhotos(photosEntites);
                for(PhotoEntity photoEntity: photosEntites){
                    Log.e("Photo entity", photoEntity.toString());
                }
            }
        });

        setPhotosAdapter();
   /*    recyclerViewPhotosList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            FloatingActionButton mFloatingActionButton = rootView.findViewById(R.id.addReportFloatingButton);
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mFloatingActionButton.hide();
                } else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mFloatingActionButton.show();
                }
            }
        }); */
            return rootView;
        }
    private void setPhotosAdapter(){
        photosAdapter = new PhotosAdapter(this, mainPhoto, addReportViewModel, textInputLayout, editTextPhotoDescription);
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
                    Log.e("AddFragment", "G aleery request code poczatek");
                    if (data != null) {
                        boolean isMainPhotoSet=false;
                        if (addReportViewModel.getMainPhotoUri()!=null){
                            isMainPhotoSet=true;
                        }
                       /* if (data.getData() != null) {
                            Log.e("AddFragment", "G aleery request code jedno zdjecie");
                                Uri mImageUri = data.getData();
                                PhotoEntity photoEntity = new PhotoEntity(mImageUri);
                                if (!isMainPhotoSet) {
                                    addReportViewModel.setMainPhoto(photoEntity);
                                }
                                addReportViewModel.addPhoto(photoEntity);
                        } else { */
                            if (data.getClipData() != null) {
                                Log.e("AddFragment", "Duzo zdjec");
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    PhotoEntity photoEntity = new PhotoEntity(uri);
                                    addReportViewModel.addPhoto(photoEntity);
                                    if (!isMainPhotoSet) {
                                        addReportViewModel.setMainPhoto(photoEntity);
                                        isMainPhotoSet=true;
                                    }
                                }
                            }
                            else {
                            }
                            Log.e("Zmiana widoczno"," Dziala ");
                            textViewNoImage.setVisibility(View.GONE);
                            textInputLayout.setVisibility(View.VISIBLE);

                            Glide
                                    .with(this)
                                    .load(addReportViewModel.getMainPhotoUri().getPhotoUri())
                                    .placeholder(R.drawable.noimge)
                                    .centerCrop()
                                    .into(mainPhoto);
                            break;
                        }
                    }

            }
   // }

    public void addPhotos(View view) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reportAddButtonAddPhoto:
                addPhotos(view);
                break;
        }
    }

}
