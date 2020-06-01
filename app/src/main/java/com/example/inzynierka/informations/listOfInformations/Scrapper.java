package com.example.inzynierka.informations.listOfInformations;

import android.content.Context;
import android.util.Log;

import com.example.inzynierka.Database.informations.EventDetails;
import com.example.inzynierka.informations.descriptionViews.mapViews.LocalizationParser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@lombok.extern.java.Log
public class Scrapper {
    private final String url = "https://historia.org.pl/2020/01/04/kalendarz-wydarzen-historycznych-2020-rekonstrukcje-turnieje-zloty-konferencje-wystawy-etc/";
    private Document page;
    public static final String NO_DATA = "BRAK DANYCH";
    LocalizationParser locationParser;
    public Scrapper(){
    }
       public Scrapper(Context context){
        locationParser = new LocalizationParser(context);
    }

    public Document openDocument(){
        try {
            page = Jsoup.connect(url).get();
            return page;
        } catch (IOException e) {
            Log.e("Could open url ", url);
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getMonths(){
        List<String> headings = new ArrayList<>();
        for (Element element: page.select("h4")){
            final String heading = element.text();
            headings.add(heading);
        }
        System.out.println("Headings " + headings.toString());
        return headings;
    }
    public List<EventDetails> getInfoFromMonths(){
        List<EventDetails> info = new ArrayList<>();
        for (Element element: page.select("h4")){
            Element elementSibling = element.nextElementSibling();
            System.out.println("Element tag " + element.text());
            while(elementSibling != null && !elementSibling.tagName().equals("h4") && !elementSibling.tagName().equals("div")) {
//                System.out.println("Element " + elementSibling.text());
//                System.out.println("Strong" + elementSibling.select("strong").text());
                    String date; // while nie te strong to dodaawaj do tej daty
                System.out.println("Element sibling tag " + elementSibling.text());
                if(!elementSibling.select("strong").text().equals("")){
                    date = elementSibling.select("strong").text();
                    elementSibling = elementSibling.nextElementSibling();
                        while(elementSibling != null
                                && !containsDigit(elementSibling.select("strong").text())
                                && !elementSibling.tagName().equals("h4") && !elementSibling.tagName().equals("div")){

                            System.out.println("Element in while loop " + elementSibling.text() + " " + elementSibling.tagName());
                            if(!elementSibling.tagName().equals("p")){
                                System.out.println("Skipping element " + elementSibling.text());
                                elementSibling = elementSibling.nextElementSibling();
                            }
                            if(!elementSibling.text().equals("")){
                                String link = elementSibling.select("a[href]").attr("href");
                                String title = elementSibling.text();
                                String city = StringUtils.substringBetween(title, "[", "]");
                                if(city == null){
                                    city = NO_DATA;
                                }
                                else {

                                }
                                title = title.replace("["+city+"]", StringUtils.EMPTY);
                                title = title.replace(" - program", StringUtils.EMPTY);
                                title = title.replace("2020", StringUtils.EMPTY);
                                title = title.replace(" Zobacz", StringUtils.EMPTY);
                                title = title.replace("tegoroczny program", StringUtils.EMPTY);
                                EventDetails eventDetails = new EventDetails(title, date , city, link);
                                info.add(eventDetails);
                                System.out.println("Scrapped entity " + eventDetails);
                                elementSibling = elementSibling.nextElementSibling();
                        }
                            else {
                                elementSibling = elementSibling.nextElementSibling();
                            }
                    }
                }

//                    String date = elementSibling.text();
//                    elementSibling = elementSibling.nextElementSibling();
//                    String title = elementSibling.text();
//                    EventDetails eventDetails = new EventDetails(title, date, "", "");
//                    info.add(eventDetails);
            }
        }
        for(EventDetails text : info){
            log.info("SCRAPPED ENTITY " + text);
        }
        return info;
    }
    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }
}
