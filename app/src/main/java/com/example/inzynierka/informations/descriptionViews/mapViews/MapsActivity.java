package com.example.inzynierka.informations.descriptionViews.mapViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inzynierka.Adapters.InformationsAdapter;
import com.example.inzynierka.Database.informations.EventDetails;
import com.example.inzynierka.Database.informations.EventDetailsRepository;
import com.example.inzynierka.Database.informations.LocalizationEntity;
import com.example.inzynierka.R;
import com.example.inzynierka.informations.descriptionViews.detailsViews.EventDetailsActivity;
import com.example.inzynierka.addons.CustomBottomDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.fragment.app.FragmentActivity;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;


@Log
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, CustomBottomDialog.ItemClickListener {
    private static final String EMPTY_STRING = "";
    public static final String BUNDLE_EVENT_TITLE = "eventTitle";
    private static final String BUNDLE_EVENT_URL = "url";
    private GoogleMap map;
    private EventDetailsRepository repository;
    private Activity activity;
    private CustomBottomDialog customBottomDialog;
    private static LatLng POLAND_LAT_LANG = new LatLng(52.237049,19.017532);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        repository = new EventDetailsRepository(getApplication());
        activity = this;
        customBottomDialog = new CustomBottomDialog(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initView();
    }

    private LatLng getCityCoords(String city) {
        LocalizationParser parser = new LocalizationParser(this);
        LatLng cityCoords = parser.getCoordsByCityName(city);
        log.info(city + " CORDS " + cityCoords.toString());
        return cityCoords;

    }

    private String getCityBundle() {
        Intent intent = getIntent();
        String city = intent.getStringExtra(InformationsAdapter.CITY_BUNDLE);

        if(city == null){
            return EMPTY_STRING;
        }

        log.info("RECIVED CITY NAME " + city);
        return city;
    }

    private void initView() {

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onItemClick(View item) {
        TextView tv = (TextView) item;
        String title = tv.getText().toString();
        log.info("Clicked text views " + title);
        new ActivityDescriptionRequestTask(title).execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        activity.runOnUiThread(() -> new LocalizationRequestTask().execute());
        setInfoWindowAdapter();
        map.setOnInfoWindowClickListener(marker -> {
            String markerSnipper = marker.getSnippet();
            List<String> eventTitles = Arrays.asList(markerSnipper.split("\n" + "\n"));
            log.info("Clicked titles " + eventTitles.toString());
            customBottomDialog.addTextViewMessage(eventTitles);
        });

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(POLAND_LAT_LANG, (float)5.5));
    }

    public class LocalizationRequestTask extends AsyncTask<Void, Void, List<EventDetails>> {

        public LocalizationRequestTask() {
        }

        @Override
        protected List<EventDetails> doInBackground(Void... voids) {
            return repository.getAllWithLocalization()
                    .stream()
                    .filter(entity -> entity.getLocalizationEntity().getLatitude() != 0
                            && entity.getLocalizationEntity().getLongitude() != 0)
                    .collect(Collectors.toList());
        }
            @Override
            protected void onPostExecute(List<EventDetails> eventDetails) {
                if(eventDetails.isEmpty()){
                    return;
                }

                Set<LocalizationEntity> localizations = getDistinctLocalizations(eventDetails);
                Map<LocalizationEntity, List<EventDetails>> localizationEvents =
                        getLocalizationEvents(localizations, eventDetails);

                eventDetails.forEach(event ->{
                    log.info(event.getLocalizationEntity().getName() + " COORDS FOR MAP " + event.getLocalizationEntity().getLongitude() +
                            "," + event.getLocalizationEntity().getLatitude());
                    LatLng latLng = getEventLatLng(event);
                    String eventsTitles = getLocalizationEventsTitles(localizationEvents, event);

                    log.info("Localization " + event.getLocalizationEntity().getName() + " Events " + eventsTitles);

                    map.addMarker(new MarkerOptions().position(latLng)
                            .title(event.getLocalizationEntity().getName())
                            .snippet(eventsTitles));
                });
            }
    }

    private Set <LocalizationEntity> getDistinctLocalizations(List<EventDetails> details){
        return details
                .stream()
                .map(EventDetails::getLocalizationEntity)
                .collect(Collectors.toSet());
    }

    private Map<LocalizationEntity, List<EventDetails>> getLocalizationEvents(Set<LocalizationEntity> localizations,
                                                                              List<EventDetails> details){

        Map<LocalizationEntity, List<EventDetails>> localizationEvents = new HashMap<>();

        localizations.forEach(localizationEntity -> {
            List<EventDetails> eventDetailsList = new ArrayList<>();
            details.forEach(event -> {
                log.info("Event details FOR MAP " + event);
                if(event.getLocalizationEntity().getName().equals(localizationEntity.getName())){
                    eventDetailsList.add(event);
                }
            });
            localizationEvents.put(localizationEntity, eventDetailsList);
        });
        return localizationEvents;
    }
    private LatLng getEventLatLng(EventDetails eventDetails) {
        return new LatLng(eventDetails.getLocalizationEntity().getLatitude(),
                eventDetails.getLocalizationEntity().getLongitude());
    }

    private String getLocalizationEventsTitles(Map<LocalizationEntity, List<EventDetails>>localizationEvents, EventDetails event){
        String eventTitles = localizationEvents.get(event.getLocalizationEntity())
                .stream()
                .map(eventDetai ->{
                    return eventDetai.getTitle() + "\n" + "\n";
                })
                .collect(Collectors.joining());
        return removeWhiteSpacesFromStringEnd(eventTitles);
    }

    private String removeWhiteSpacesFromStringEnd(String eventTitles){
        StringBuilder temp = new StringBuilder(eventTitles);
        for( int i = temp.length() - 1 ; i >= 0; i--){
            if(temp.charAt(i) == '\n'){
                temp.deleteCharAt(i);
            }else{
                break;
            }
        }
        return temp.toString();
    }

    private void setInfoWindowAdapter(){
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context mContext = activity.getApplicationContext();
                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    @AllArgsConstructor
    public class ActivityDescriptionRequestTask extends AsyncTask<Void, Void, String>{
        String title;

        @Override
        protected String doInBackground(Void... voids) {
            EventDetails details = repository.findEventsByTitle(title);
            String detailsUrl = details.getDescription();
            log.info("Details url " + detailsUrl);
            return detailsUrl;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null && !s.isEmpty()) {
                log.info("Loading activity for url " + s);
                Intent intent = new Intent(activity, EventDetailsActivity.class);
                intent.putExtra(BUNDLE_EVENT_TITLE, title);
                intent.putExtra(InformationsAdapter.URL_BUNDLE, s);
                startActivity(intent);
            }
            else {
                log.info("Details not found");
                Snackbar.make(findViewById(android.R.id.content).getRootView(),
                        "Brak informacji dotyczących wydarzenia", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
