package com.example.inzynierka.informations.listOfInformations;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.inzynierka.Adapters.InformationsAdapter;
import com.example.inzynierka.Database.informations.EventDetails;
import com.example.inzynierka.Database.informations.EventDetailsRepository;
import com.example.inzynierka.Database.informations.LocalizationEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.informations.descriptionViews.mapViews.LocalizationParser;
import com.example.inzynierka.informations.descriptionViews.mapViews.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lombok.extern.java.Log;

@Log
public class InformationsActivity extends AppCompatActivity {
    Button refresh;
    Button openMap;
    LinearLayout loadingLayout;
    List<EventDetails> details;
    InformationsAdapter informationsAdapter;
    RecyclerView informationsView;
    TextInputEditText searchView;
    InfoFilter filter;
    EventDetailsRepository repository;
    LocalizationParser localizationParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);
        filter = new InfoFilter();
        repository = new EventDetailsRepository(getApplication());
        localizationParser = new LocalizationParser(this);
        initView();
        new RoomRequestTask().execute();
    }
    private void initView(){
        initSearchView();
        initRecyclerView();
        initAdapter();
        initButtons();
        initLayouts();
    }

    private void initLayouts() {
        loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
    }

    private void initButtons() {
        refresh = (Button) findViewById(R.id.refreshButton);

        refresh.setOnClickListener(view -> {
            loadingLayout.setVisibility(View.VISIBLE);
            informationsView.setVisibility(View.GONE);
            new ScrapperRequestTask().execute();
        }
            );

        openMap = (Button) findViewById(R.id.seeOnMapButton);
        openMap.setOnClickListener(view -> {
                    Snackbar.make(view, "Proszę poczekąć. Wydarzenia są ładowane", Snackbar.LENGTH_SHORT).show();
                }
        );
    }

    private void initRecyclerView() {
        informationsView = (RecyclerView) findViewById(R.id.informationsRV);
    }

    private void initAdapter() {
        informationsAdapter = new InformationsAdapter(informationsView, this.getApplicationContext());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        informationsView.setLayoutManager(verticalLayoutManager);
        informationsView.setAdapter(informationsAdapter);
    }

    private void initSearchView() {
        searchView = (TextInputEditText) findViewById(R.id.infoSearch);
//        EditText searchEditText = (EditText) searchView.findViewById(android.support..R.id.search_src_text);
//        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchView.addTextChangedListener(new TextWatcher() {
                                              @Override
                                              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                              }

                                              @Override
                                              public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                  if(details!=null){
                                                 List<EventDetails> newDetails = filter.filterdByTitle(s.toString());
                                                 informationsAdapter.setDetails(newDetails);
                                                  }
                                              }

                                              @Override
                                              public void afterTextChanged(Editable s) {

                                              }
                                          });
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if(details!=null){
//                    List<EventDetails> newDetails = filter.filterdByTitle(newText);
//                    informationsAdapter.setDetails(newDetails);
//                }
//                return true;
//            }
//        });
    }

    private void clearEventsTable(){
        log.info("CLEARING EVENTS DB");
        repository.deleteAll();
    }

    private void saveToDatabase(List<EventDetails> eventDetails){
        log.info("SAVING EVENTS TO DB");
        eventDetails.stream()
                .forEach(details->{
                    String localization = details.getLocalization();
                    LatLng latLng = localizationParser.getCoordsByCityName(details.getLocalization());
                    LocalizationEntity localizationEntity = new LocalizationEntity(localization,latLng.longitude, latLng.latitude);
                    details.setLocalizationEntity(localizationEntity);
                    repository.insert(details);
              //      log.info("SAVED ENTITY " + details.toString());
                });
    }

    public class ScrapperRequestTask extends AsyncTask<Void, Void, List<EventDetails>> {
        Scrapper scrapper;

        public ScrapperRequestTask() {
            this.scrapper = new Scrapper(getApplicationContext());
            openMap.setOnClickListener(view -> {
                        Snackbar.make(view, "Proszę czekać. Wydarzenia są ładowane", Snackbar.LENGTH_SHORT).show();
                    }
            );
        }

        @Override
        protected List<EventDetails> doInBackground(Void... voids) {
            scrapper.openDocument();
            clearEventsTable();
            List<EventDetails> eventDetails = scrapper.getInfoFromMonths();
            saveToDatabase(eventDetails);
            return eventDetails;
        }
        @Override
        protected void onPostExecute(List<EventDetails> eventDetails) {
            new RoomRequestTask().execute();
        }
    }
    public class RoomRequestTask extends AsyncTask<Void, Void, List<EventDetails>> {

        public RoomRequestTask() {
        }

        @Override
        protected List<EventDetails> doInBackground(Void... voids) {
            log.info("GET EVENTS FROM DB");
            List<EventDetails> eventDetails = repository.getAll();

            if(eventDetails == null || eventDetails.size() == 0){
                new ScrapperRequestTask().execute();
                return new ArrayList<>();
            }

            List<LocalizationEntity> localizations = repository.getAllLocalizations();
            for (int i=0 ;i<localizations.size(); i++){
                 LocalizationEntity localizationEntity = localizations.get(i);
                 for (int j=0; j<eventDetails.size(); j++){
                    EventDetails details = eventDetails.get(j);
                    if(details.getLocalizationId() == localizationEntity.getId()){
                        details.setLocalizationEntity(localizationEntity);
                    }
                }
            }

            return eventDetails;
        }
        @Override
        protected void onPostExecute(List<EventDetails> eventDetails) {
            if(eventDetails.isEmpty()){
                return;
            }
            details = eventDetails;
            filter.setDetails(eventDetails);
            informationsAdapter.setDetails(eventDetails);
            informationsView.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);

            openMap.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            });
        }
    }
}

