package com.example.inzynierka.Database.informations;

import android.app.Application;

import com.example.inzynierka.Database.AppRoomDatabase;

import java.util.List;

public class EventDetailsRepository {
    private EventDetailsDao detailsDao;
    private LocalizationDao localizationDao;

    public EventDetailsRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        detailsDao = db.eventDetailsDao();
        localizationDao = db.localizationDao();
    }
    public void insert(EventDetails eventDetails){
        long id = localizationDao.insert(eventDetails.getLocalizationEntity());
        eventDetails.setLocalizationId(id);
        detailsDao.insert(eventDetails);
    }
    public void deleteAll(){
        localizationDao.deleteAll();
        detailsDao.deleteAll();
    }

    public List<EventDetails> getAll(){
        return detailsDao.getAll();

    }


    public List<EventDetails> getAllWithLocalization(){
        List<LocalizationEntity>  localizations = localizationDao.getAll();
        List<EventDetails> eventDetails = detailsDao.getAll();
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
    public List<LocalizationEntity> getAllLocalizations(){
        return localizationDao.getAll();
    }

    public EventDetails findEventsByTitle(String title) {
        return detailsDao.getEventByTitle(title);
    }
}


