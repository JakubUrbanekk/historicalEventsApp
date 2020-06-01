package com.example.inzynierka.informations.descriptionViews.detailsViews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Adapters.InformationsAdapter;
import com.example.inzynierka.R;
import com.example.inzynierka.informations.descriptionViews.mapViews.MapsActivity;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import lombok.extern.java.Log;

@Log
public class EventDetailsActivity extends AppCompatActivity {
    private String url;
    private String title;
    LinearLayout layout;
    TextView detailsTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        url = getUrlFromIntent();
        title = getEventTitleFromIntent();

        initView();
    }

    private void initView() {
        layout = findViewById(R.id.mainContainer);
        detailsTitle = findViewById(R.id.eventDetailsTitle);
        detailsTitle.setText(title);
        new DetailsScrapperRequestTask(url).execute();
    }

    public void createImageView(Bitmap bitmap){
        ImageView imageView = new ImageView(this);
        Glide
                .with(this)
                .load(bitmap)
                .into(imageView);

        layout.addView(imageView);
    }

    public void createHeaderTextView(String message) {
        Activity activity = this;
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(20);
        log.info("Header text " + message);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.quantum_black_100));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        layout.addView(textView, params);
    }
    public void creatMediumTextView(String message) {
        Activity activity = this;
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        log.info("Header text " + message);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.quantum_black_100));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        layout.addView(textView, params);
    }


    public void createSmallTextView(String message) {
        Activity activity = this;
        TextView textView = new TextView(this);
        log.info("Small text " + message);
        textView.setText(message);
        textView.setTextSize(12);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.quantum_black_100));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        layout.addView(textView, params);
    }

    private String getUrlFromIntent(){
        Intent intent = getIntent();
        return intent.getStringExtra(InformationsAdapter.URL_BUNDLE);
    }

    private String getEventTitleFromIntent(){
        Intent intent = getIntent();
        return intent.getStringExtra(MapsActivity.BUNDLE_EVENT_TITLE);
    }

    public class DetailsScrapperRequestTask extends AsyncTask<Void, Void, DescriptionEntity> {
        DetailsScrapper detailsScrapper;

        public DetailsScrapperRequestTask(String url) {
            detailsScrapper = new DetailsScrapper(url);
        }
        @Override
        protected DescriptionEntity doInBackground(Void... voids) {
            DescriptionEntity entity = detailsScrapper.scrap();
            return entity;
        }

        @Override
        protected void onPostExecute(DescriptionEntity descriptionEntity) {
            log.info("First rendered p " + descriptionEntity.getFirstParagraph());
            creatMediumTextView(descriptionEntity.getFirstParagraph());
            createImageView(descriptionEntity.getFirstImage());
            List<String> headings = descriptionEntity.getHeadings();
            List<String> paragraphs = descriptionEntity.getParagraphs();
            for (int i=0; i<headings.size(); i++){
                createHeaderTextView(headings.get(i));
                createSmallTextView(paragraphs.get(i));
            }
        }
    }
}
