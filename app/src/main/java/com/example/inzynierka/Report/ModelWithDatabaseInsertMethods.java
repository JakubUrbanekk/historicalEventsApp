package com.example.inzynierka.Report;

import com.example.inzynierka.Database.Photo.PhotoEntity;
import com.example.inzynierka.Database.Report.ReportEntity;

public interface ModelWithDatabaseInsertMethods {
    public long insertPhoto(PhotoEntity photoEntity);
    public long insertReport(ReportEntity reportEntity);
}
