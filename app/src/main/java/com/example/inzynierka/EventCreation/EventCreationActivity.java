package com.example.inzynierka.EventCreation;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inzynierka.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class EventCreationActivity extends AppCompatActivity implements PlacesAutoCompleteAdapter.ClickListener{

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private EditText placesSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        Places.initialize(this, "");
        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        placesSearch = (EditText) findViewById(R.id.place_search);
        placesSearch.addTextChangedListener(filterTextWatcher);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    @Override
    public void click(Place place) {
        placesSearch.setText(place.getAddress());
        Toast.makeText(this, place.getAddress()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }
}
