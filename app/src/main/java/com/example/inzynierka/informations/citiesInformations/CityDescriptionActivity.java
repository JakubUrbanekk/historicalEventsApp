package com.example.inzynierka.informations.citiesInformations;

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

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import lombok.extern.java.Log;

@Log
public class CityDescriptionActivity extends AppCompatActivity {
    private LinearLayout paragraphsLayout;
    private TextView titleTextView;
  //  private ImageView mapImageView;
    private ImageView photosImageView;
  //  private TextView mapTextView;
    private TextView photosTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_description);
        initView();
        scrapInfoInitView();
    }

    private void scrapInfoInitView() {
        String url = getUrlBundle();
        new CityScrapperRequestTask(url).execute();
    }

    private void initView(){
        paragraphsLayout = (LinearLayout) findViewById(R.id.cityParagraphsLayout);
        titleTextView = (TextView) findViewById(R.id.cityDescriptionTitle);
       // mapImageView = (ImageView) findViewById(R.id.cityOnMap);
        photosImageView = (ImageView) findViewById(R.id.cityPhotos);
      //  mapTextView = (TextView) findViewById(R.id.cityMapTextView);
        photosTextView = (TextView) findViewById(R.id.cityPhotosTextView);
        String title = getCityBundle();
        setTitleTextView(title);
    }

    public void createMediumTextView(String message) {
        Activity activity = this;
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
     //   log.info("Header text " + message);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.quantum_black_100));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        paragraphsLayout.addView(textView, params);
    }

    private void setTitleTextView(String textView){
        titleTextView.setText(textView);
    }
    private String getCityBundle() {
        Intent intent = getIntent();
        String city = intent.getStringExtra(InformationsAdapter.CITY_BUNDLE);
        log.info("RECIVED CITY NAME " + city);
        return city;
    }

    public void loadToImageView(ImageView imageView, Bitmap bitmap){
        Glide
                .with(this)
                .load(bitmap)
                .into(imageView);
    }

    private String getUrlBundle(){
        Intent intent = getIntent();
        String bundleCityUrl = intent.getStringExtra(InformationsAdapter.BUNDLE_WIKI_URL);
        log.info("Url " + bundleCityUrl);
        return bundleCityUrl;
    }

    public class CityScrapperRequestTask extends AsyncTask<Void, Void, WikipediaEntity> {
        WikipediaScrapper wikipediaScrapper;

        public CityScrapperRequestTask(String url) {
            wikipediaScrapper = new WikipediaScrapper(url);
        }
        @Override
        protected WikipediaEntity doInBackground(Void... voids) {
            WikipediaEntity entity = wikipediaScrapper.scrap();
            log.info("Scrapper entity " + entity);
            return entity;
        }

        @Override
        protected void onPostExecute(WikipediaEntity wikipediaEntity) {
            List<String> paragraphs = wikipediaEntity.getParagraphs();
            for (int i=0; i<paragraphs.size(); i++){
                createMediumTextView(paragraphs.get(i));
            }
      //      Bitmap mapBitmap = wikipediaEntity.getMapPhotoBitmap();
            Bitmap cityPhotoBitmap = wikipediaEntity.getCityPhotoBitmap();
      //      String mapText = wikipediaEntity.getMapPhotoDescription();
            String cityText = wikipediaEntity.getCityPhotoDescription();
            log.info("Received entity " + wikipediaEntity.getCityPhoto());
            log.info("Map entity " + wikipediaEntity.getMapPhoto());
         //   loadToImageView(mapImageView, mapBitmap);
          //  loadTextView(mapTextView, mapText);

            if(cityPhotoBitmap != null) {
                loadToImageView(photosImageView, cityPhotoBitmap);
            }

            if(cityText != null) {
                loadTextView(photosTextView, cityText);
            }

        }
    }

    private void loadTextView(TextView photosTextView, String cityText) {
        photosTextView.setText(cityText);
    }
}
