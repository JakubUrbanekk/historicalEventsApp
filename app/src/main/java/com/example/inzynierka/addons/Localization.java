package com.example.inzynierka.utils;

import com.example.inzynierka.Database.Report.ReportEntity;

public class Localization {
    String name;

    public void test(String reportTitle) {
        ReportEntity newReportEntity = ReportEntity.builder()
                .reportTitle(reportTitle)
                .build();

        newReportEntity.describeContents();
    }
}
