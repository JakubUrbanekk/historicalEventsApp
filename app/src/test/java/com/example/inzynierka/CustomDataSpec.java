package com.example.inzynierka;

import com.example.inzynierka.Database.Report.ReportEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomDataSpec {
    CustomData customData;
    @BeforeEach
    void init (){
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReportDate("12-11-2019");
        CustomData customData = new CustomData(reportEntity.getReportDate());
    }
    @Test
    void isMonthValueEqualsToTwelveIfDateStringHaveTwelveMonthValue(){
        customData.setMonth();
    }
    @Test
    void isDateSplitIntoSubstringCorrectly(){
        customData.convertStringDateToStringArray();
    }
}
