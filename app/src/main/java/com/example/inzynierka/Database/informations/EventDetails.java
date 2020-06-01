package com.example.inzynierka.Database.informations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@EqualsAndHashCode
@Entity(tableName = "events", foreignKeys = @ForeignKey(entity = LocalizationEntity.class,
        parentColumns = "id",
        childColumns = "localization_id",
        onDelete = ForeignKey.CASCADE))
public class EventDetails {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long eventId;
    private String title;
    private String date;
    @Ignore
    String localization;
    String description;
    @Ignore
    LocalizationEntity localizationEntity;
    @ColumnInfo(name = "localization_id")
    private long localizationId;

    public EventDetails(String title, String date, String localization, String description) {
        this.title = title;
        this.date = date;
        this.localization = localization;
        this.description = description;
    }
}
