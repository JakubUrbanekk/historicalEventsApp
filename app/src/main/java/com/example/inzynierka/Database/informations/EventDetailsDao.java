package com.example.inzynierka.Database.informations;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface EventDetailsDao {
    @Insert
    long insert(EventDetails eventDetails);

    @Query("DELETE FROM events")
    void deleteAll();

    @Query("SELECT * from events")
    List<EventDetails> getAll();

    @Query("SELECT * from events WHERE title = :title")
    EventDetails getEventByTitle(String title);

}
