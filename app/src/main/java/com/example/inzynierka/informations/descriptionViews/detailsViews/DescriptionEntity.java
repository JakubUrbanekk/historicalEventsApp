package com.example.inzynierka.informations.descriptionViews.detailsViews;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DescriptionEntity implements Serializable {
    private final String EMPTY_STRING = "";
    private final String SPLITTER = "ASDASDASDASDASDSDA";
    private List<String> headings;
    private List<String> paragraphs;
    private List<String> boldedParagraphs;
    private String firstParagraph;
    private Bitmap firstImage;

    public DescriptionEntity(List<String> headings, List<String> paragraphs,
                             List<String> boldedParagraphs, Bitmap firstImage, String firstParagraph) {
        this.headings = headings;
        this.paragraphs = paragraphs;
        this.boldedParagraphs = boldedParagraphs;
        this.firstImage = firstImage;
        this.firstParagraph = firstParagraph;
//        for (String s : paragraphs){
//            for (String bolded : boldedParagraphs){
//                if (s.contains(bolded)){
//                    s = s.replace(bolded, SPLITTER);
//                }
//            }
//        }
//        for (String s : paragraphs){
//            String [] newParagraphs =
//        }

    }

    public void addHeading(String s){
        headings.add(s);
    }
    public void addParagraph(String s){
        paragraphs.add(s);
    }

}
