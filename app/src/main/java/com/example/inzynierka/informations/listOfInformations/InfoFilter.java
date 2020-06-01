package com.example.inzynierka.informations.listOfInformations;

import com.example.inzynierka.Database.informations.EventDetails;

import java.util.ArrayList;
import java.util.List;

public class InfoFilter {
    List<EventDetails> details;

    public InfoFilter() {

    }

    public List<EventDetails> filterdByTitle(String title){
        title = title.toLowerCase();
        List<EventDetails> results = new ArrayList<>();

        for (EventDetails eventDetails : details){
            String eventTitle = eventDetails.getTitle().toLowerCase();
            if (eventTitle.contains(title)){
                results.add(eventDetails);
            }
        }
        return results;
    }
    public void setDetails(List<EventDetails> details){
        this.details = details;
    }
}
