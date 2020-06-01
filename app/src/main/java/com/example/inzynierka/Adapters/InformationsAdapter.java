package com.example.inzynierka.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inzynierka.Database.informations.EventDetails;
import com.example.inzynierka.R;
import com.example.inzynierka.informations.citiesInformations.CityDescriptionActivity;
import com.example.inzynierka.informations.citiesInformations.WikipediaScrapper;
import com.example.inzynierka.informations.descriptionViews.detailsViews.EventDetailsActivity;
import com.example.inzynierka.informations.descriptionViews.mapViews.MapsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.AllArgsConstructor;

@lombok.extern.java.Log
public class InformationsAdapter extends RecyclerView.Adapter<InformationsAdapter.ViewHolder> {
    List<EventDetails> detailsList;
    private View view;
    private Context context;
    public static final String URL_BUNDLE = "url";
    public static final String CITY_BUNDLE = "city";
    public static final String BUNDLE_WIKI_URL = "wikiUrl";
    private static final String WIKIPEDIA_URL = "https://pl.wikipedia.org/wiki/";
    public InformationsAdapter(List<EventDetails> details){
        detailsList = details;
    }

    public InformationsAdapter(View view){
        detailsList = new ArrayList<>();
        this.view = view;
    }

    public InformationsAdapter(RecyclerView informationsView, Context applicationContext) {
        detailsList = new ArrayList<>();
        this.view = informationsView;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.informations_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventDetails details = detailsList.get(position);
        holder.itemView.setOnClickListener(view -> openEventDetailsActivity(details.getDescription(), details.getTitle()));

        log.info("RENDERING VIEW FOR " + details);
        String localization = details.getLocalization();
        if (localization == null || localization.isEmpty()){
            localization = details.getLocalizationEntity().getName();
        }

        holder.localization.setText(localization);
        String finalLocalization = localization;
        holder.localization.setOnClickListener(view -> openMapActivity(finalLocalization));
        holder.title.setText(details.getTitle());
        holder.date.setText(details.getDate());

    }

    private void openMapActivity(String city) {
        log.info("Opening map activity");
        new ActivityCityDescription(city).execute();
    }

    private void openEventDetailsActivity(String descriptionUrl, String title) {
        log.info("STARTING EVENT DESCRIPTION ACTIVITY");
        if (descriptionUrl.isEmpty()){
            log.info("DESCRIPTION DOES NOT EXISTS");
            Snackbar.make(view, "Brak informacji dotyczących wydarzenia", Snackbar.LENGTH_SHORT).show();
        }
        else {
            log.info("DESCRIPTION URL " + descriptionUrl);
            Intent openDetailsActivity = new Intent(context, EventDetailsActivity.class);
            openDetailsActivity.putExtra(URL_BUNDLE, descriptionUrl);
            openDetailsActivity.putExtra(MapsActivity.BUNDLE_EVENT_TITLE, title);
            context.startActivity(openDetailsActivity);
        }
    }
    @Override
    public int getItemCount() {
        return detailsList.size();
    }

     public class ViewHolder extends RecyclerView.ViewHolder {
        TextView localization;
        TextView title;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.infoTitleTV);
            localization = (TextView) itemView.findViewById(R.id.infoLocalizationTV);
            date = (TextView) itemView.findViewById(R.id.infoDateTV);
        }
     }

    public void setDetails(List<EventDetails> detailsEntities){
        this.detailsList.clear();
        this.detailsList.addAll(detailsEntities);
        this.notifyDataSetChanged();
    }
    @AllArgsConstructor
    public class ActivityCityDescription extends AsyncTask<Void, Void, Boolean> {
        String city;

        @Override
        protected Boolean doInBackground(Void... voids) {
            String fullWikiUrl = WIKIPEDIA_URL + city;
            log.info("Scrapping " + fullWikiUrl);
            WikipediaScrapper wikipediaScrapper = new WikipediaScrapper(fullWikiUrl);
            return wikipediaScrapper.checkIfExists();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            String fullWikiUrl = WIKIPEDIA_URL + city;
            Intent openMapIntent = new Intent(context, CityDescriptionActivity.class);
            openMapIntent.putExtra(BUNDLE_WIKI_URL, fullWikiUrl);
            openMapIntent.putExtra(CITY_BUNDLE, city);
            if (s) {
                context.startActivity(openMapIntent);
            }
            else {
                Snackbar.make(view, "Brak informacji dotyczących miejsca", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
