package com.example.inzynierka.informations.citiesInformations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@lombok.extern.java.Log
public class WikipediaScrapper {
    private final String URL;
    private Document page;

    public Boolean checkIfExists(){
        try {
            page = Jsoup.connect(URL).get();
            return true;
        } catch (IOException e) {
            Log.e("Could open url ", URL);
            e.printStackTrace();
            return false;
        }
    }

    public void opeenDocument(){
        try {
            page = Jsoup.connect(URL).get();
        } catch (IOException e) {
            Log.e("Could open url ", URL);
            e.printStackTrace();
        }
    }

    public WikipediaEntity scrap(){
        opeenDocument();
        WikipediaEntity entity = new WikipediaEntity(scrapParagraphs(), getCityPhotos(), getMap());
        log.info("entity " + entity);
        return entity;
    }

    private List<String> scrapParagraphs() {
        List<String> paragraphs = new ArrayList<>();
        Element element = page.select("div[class=mw-parser-output]").select("p").first();
        while (element != null && element.tagName().equals("p")){
            String elementText = element.text();
            elementText = elementText.replaceAll("\\[.*?]", "");
            paragraphs.add(elementText);
            element = element.nextElementSibling();
        }

        log.info("Parahraphs" + paragraphs);
        return paragraphs;
    }

    private CityPhotoEntity getCityPhotos(){
        Elements img = page.select("img[alt=Ilustracja]");
        String imgSrc = img.attr("src");
        if (!imgSrc.contains("https:")){
            imgSrc = "https:" + imgSrc;
        }
        log.info("Map src " + imgSrc);
        InputStream input = null;
        try {
            input = new java.net.URL(imgSrc).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        String cityDescription = getCityPhotosString();
        CityPhotoEntity cityPhotosEntity = new CityPhotoEntity(bitmap, cityDescription);
        log.info("Photos city entity " + cityPhotosEntity);
        return cityPhotosEntity;
    }

    private CityPhotoEntity getMap(){
        Element img = page.select("div.mapa-lokalizacyjna").select("img").first();
        String imgSrc = img.attr("src");
        if (!imgSrc.contains("https:")){
            imgSrc = "https:" + imgSrc;
        }
        log.info("Map src " + imgSrc);
        InputStream input = null;
        try {
            input = new java.net.URL(imgSrc).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        String mapDescription = getMapString();
        CityPhotoEntity mapEntity = new CityPhotoEntity(bitmap, mapDescription);
        log.info("Map entity " + mapEntity);
        return mapEntity;
    }

    private String getMapString(){
        String bitmapText = page.select("div[style=color:gray;font-weight:bold]").first().text();
        log.info("bitmap text" + bitmapText);
        return bitmapText;
    }
    private String getCityPhotosString(){
        Element img = page.select("img[alt=Ilustracja]").first();
        log.info("City photos image " + img);
        log.info("Img parent text " + img.parent().attr("title"));
//        List<String> elements = img.parent().siblingElements().eachText();
 //       log.info("City photos title " + elements);
        String element = img.parent().attr("title");
//                elements.stream()
//                .collect(Collectors.joining(" "));
        log.info("Photo " + element);
        return element;
    }
}
