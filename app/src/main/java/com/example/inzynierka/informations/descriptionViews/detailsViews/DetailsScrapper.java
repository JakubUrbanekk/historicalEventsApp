package com.example.inzynierka.informations.descriptionViews.detailsViews;

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

@lombok.extern.java.Log
public class DetailsScrapper {
    private final String URL;
    private Document page;

    public DetailsScrapper(String url){
        URL = url;
    }

    public Document openDocument(){
        try {
            page = Jsoup.connect(URL).get();
            return page;
        } catch (IOException e) {
            Log.e("Could open url ", URL);
            e.printStackTrace();
        }
        return null;
    }

    public DescriptionEntity scrap(){
        openDocument();
        Bitmap firstImage = getFirstImage();
        List<String> boldedParagraphs = new ArrayList<>();
        boldedParagraphs.addAll(getBoldedParagraphs());
        Elements headings = getHeadings();
        String firstParagaph = getFirstParagaph();
        List<String> paragraphs = new ArrayList<>(getAllParagraphs(headings));
        DescriptionEntity descriptionEntity = new DescriptionEntity(getTexts(headings), paragraphs, boldedParagraphs, firstImage, firstParagaph);
        log.info("Created entity " + descriptionEntity);
        return descriptionEntity;
    }

    public Elements getHeadings(){
        Elements elems = page.select("h5");
        Elements elements = new Elements();
       // log.info("elems size " + elems.size());
        for (int i=0; i<elems.size()-2; i++){
            if (!elems.get(i).text().isEmpty()) {
                log.info("Heading text " + elems.get(i).text());
                elements.add(elems.get(i));
            }

        }
        log.info("Headings " + elements);

        return elements;
    }

    public List<String> getTexts(Elements elements){
        return elements.eachText();
    }

    private Bitmap getFirstImage(){
        Elements elemImages = page.select("img[src$=.jpg]");
        Element img = elemImages.first();
        String imgSrc = img.attr("src");
        InputStream input = null;
        try {
            input = new java.net.URL(imgSrc).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(input);
    }

    private String getFirstParagaph(){
        Elements elemImages = page.select("p");
        String elem = elemImages.first().select("Strong").text();
        log.info("First paragraph " + elem);
        return elem;
    }

    private List<String> getAllParagraphs(Elements headings){
        List<String> paragraphs = new ArrayList<>();
        headings.forEach(element -> {
            element = element.nextElementSibling();
            while (!element.nextElementSibling().hasText()){
                element = element.nextElementSibling();
        }
            paragraphs.add(element.text());
        });

        return paragraphs;
    }
    private List<String> getBoldedParagraphs(){
        Elements allParagraphs = page.select("p").select("Strong");
        List<String> paragraphs = allParagraphs.eachText();
        log.info("Bolded paragraphs " + paragraphs.toString());
        return paragraphs;
    }
}
