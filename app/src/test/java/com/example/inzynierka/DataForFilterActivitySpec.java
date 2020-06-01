package com.example.inzynierka;

import com.example.inzynierka.Database.Report.ReportEntity;
import com.example.inzynierka.Report.ListOfReports.Filter.DataForFilterActivityExtractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DataForFilterActivitySpec {
    ReportEntity reportEntity1;
    ReportEntity reportEntity2;
    ReportEntity reportEntity3;
    ReportEntity reportEntity4;
    DataForFilterActivityExtractor dataForFilterActivityExtractor;
    List<ReportEntity> reports;

    @BeforeEach
    void init(){
        reports = new ArrayList<>();
        reportEntity1 = new ReportEntity();
        reportEntity2 = new ReportEntity();
        reportEntity3 = new ReportEntity();
        reportEntity4 = new ReportEntity();
        reports.add(reportEntity1);
        reports.add(reportEntity2);
        reports.add(reportEntity3);
        reports.add(reportEntity4);
        dataForFilterActivityExtractor = new DataForFilterActivityExtractor(reports);
    }
    @Test
    void getAllReportsLocalizations(){
        reportEntity1.setReportLocalization("Wroclaw");
        reportEntity2.setReportLocalization("Warszawa");
        reportEntity3.setReportLocalization("");
        reportEntity4.setReportLocalization("Wroclaw");
        List<String> result = dataForFilterActivityExtractor.getReportsLocalizations();
        List<String> expected = Arrays.asList("Wybierz lokalizacje", "Wroclaw", "Warszawa");
        assertThat("Zwrocono nieprawidlowe miasta", result, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void getAllReportYears(){
        reportEntity1.setReportDate("12-11-2019");
        reportEntity2.setReportDate("12-11-2019");
        reportEntity3.setReportDate("12-11-2017");
        List <String> result = dataForFilterActivityExtractor.getReportsYears();
        List <String> expected = Arrays.asList("Wybierz rok","2019", "2017");
        assertThat("Zwrocono nieprawidlowe lata", result, containsInAnyOrder(expected.toArray()));
    }
    @Test
    void getAllReportMonths(){
        reportEntity1.setReportDate("12-09-2019");
        reportEntity2.setReportDate("12-09-2019");
        reportEntity3.setReportDate("12-11-2017");
        List <String> result = dataForFilterActivityExtractor.getMonthsFromSelectedYear("2019");
        List <String> expected = Arrays.asList("Wybierz miesiąc", "Wrzesień");
        assertThat("Zwrocono nieprawidlowe miesiace", result, containsInAnyOrder(expected.toArray()));
    }
}
