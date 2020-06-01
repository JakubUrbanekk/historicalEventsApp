package com.example.inzynierka.Report.ListOfReports.Filter;

import com.example.inzynierka.CustomDate.MonthsConverter;
import com.example.inzynierka.CustomDate.UnknownMonthException;
import com.example.inzynierka.Database.Report.ReportEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.java.Log;

@Log
public class DataForFilterActivityExtractor {
    List <ReportEntity> reports;
    static final String REPORT_LOCATION_NOT_CHOOSEN = "Wybierz lokalizacje";
    static final String REPORT_CATEGORY_NOT_CHOOSEN = "Wybierz kategorie";
    static final String REPORT_EPOKA_NOT_CHOOSEN = "Wybierz epoke";
    static final String REPORT_CLOTH_NOT_CHOOSEN = "Wybierz strój";
    static final String REPORT_WEAPON_NOT_CHOOSEN = "Wybierz broń";
    static final String REPORT_ACCESSORY_NOT_CHOOSEN = "Wybierz akcesorium";
    static final String REPORT_VEHICLE_NOT_CHOOSEN = "Wybierz pojazd";

    static final String REPORT_YEAR_NOT_CHOOSEN = "Wybierz rok";
    static final String REPORT_MONTH_NOT_CHOOSEN = "Wybierz miesiąc";
    static final String REPORT_DATE_NOT_CHOOSEN = "Wybierz okres czasu";
    static final String TIME_PREVIOUS_WEEK = "Z poprzedniego tygodnia";
    static final String TIME_PREVIOUS_MONTH = "Z poprzedniego miesiąca";
    static final String TIME_PREVIOUS_THREE_MONTHS = "Z poprzednich 3 miesięcy";
    static final String TIME_PREVIOUS_YEAR = "Z poprzedniego roku";
    public DataForFilterActivityExtractor(List<ReportEntity> reports) {
        this.reports = reports;
    }

    public List<String> getReportsLocalizations() {
        List <String> result;

        Stream<String> firstItem = Stream.of(REPORT_LOCATION_NOT_CHOOSEN);

        result = reports.stream()
                .filter(reportEntity -> reportEntity.getReportLocalization()!=null && !reportEntity.getReportLocalization().equals(""))
                .map(reportEntity -> reportEntity.getReportLocalization())
                .distinct()
                .collect(Collectors.toList());
        return Stream.concat(firstItem, result.stream()).collect(Collectors.toList());
    }

    public List<String> getReportsYears() {
        List<String> result;

        Stream<String> firstItem = Stream.of(REPORT_YEAR_NOT_CHOOSEN);

        result = reports.stream()
                .filter(reportEntity -> reportEntity.getDate()!=null)
                .map(reportEntity -> reportEntity.getDate().getYear())
                .distinct()
                .collect(Collectors.toList());
        return Stream.concat(firstItem, result.stream()).collect(Collectors.toList());
    }

    public List<String> getMonthsFromSelectedYear(String year)   {
        List<String> result;

        Stream<String> firstItem = Stream.of(REPORT_MONTH_NOT_CHOOSEN);

        result = reports.stream()
                .filter(reportEntity -> reportEntity.getDate()!=null)
                .filter(reportEntity -> reportEntity.getDate().getYear().equals(year))
                .map(reportEntity -> {
                    try {
                        return MonthsConverter.convertNumberToMonth(Integer.parseInt(reportEntity.getDate().getMonth()));
                    } catch (UnknownMonthException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .distinct()
                .collect(Collectors.toList());

        return Stream.concat(firstItem, result.stream()).collect(Collectors.toList());
    }

    public List<String> getTextForDateSpinner() {
        Stream<String> firstItem = Stream.of(REPORT_DATE_NOT_CHOOSEN);
        List<String> result = Arrays.asList(TIME_PREVIOUS_WEEK, TIME_PREVIOUS_MONTH, TIME_PREVIOUS_THREE_MONTHS, TIME_PREVIOUS_YEAR);
        return Stream.concat(firstItem, result.stream()).collect(Collectors.toList());
    }

    public Set<String> getReportCategories() {
        Stream<String> firstItem = Stream.of(REPORT_CATEGORY_NOT_CHOOSEN);
        List<String> mappedToString = reports.stream()
                .filter(reportEntity -> reportEntity.getCategory() != null)
                .map(report ->report.getCategory())
                .collect(Collectors.toList());
        Set<String> set = new HashSet<>(mappedToString);
        return Stream.concat(firstItem, set.stream()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public Set<String> getReportEpoka() {
        Stream<String> firstItem = Stream.of(REPORT_EPOKA_NOT_CHOOSEN);
        List<String> mappedToString = reports.stream()
                .filter(reportEntity -> reportEntity.getEpoka() != null)
                .map(report ->report.getEpoka())
                .collect(Collectors.toList());
        Set<String> set = new HashSet<>(mappedToString);
        return Stream.concat(firstItem, set.stream()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public Set<String> getReportWeapons() {
        Stream<String> firstItem = Stream.of(REPORT_WEAPON_NOT_CHOOSEN);
        List<String> mappedToString = reports.stream()
                .filter(reportEntity -> reportEntity.getWeapon() != null)
                .map(report ->report.getWeapon())
                .collect(Collectors.toList());
        Set<String> set = new HashSet<>(mappedToString);
        return Stream.concat(firstItem, set.stream()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public Set<String> getReportVehicles() {
        Stream<String> firstItem = Stream.of(REPORT_VEHICLE_NOT_CHOOSEN);
        List<String> mappedToString = reports.stream()
                .filter(reportEntity -> reportEntity.getVehicle() != null)
                .map(report ->report.getVehicle())
                .collect(Collectors.toList());
        Set<String> set = new HashSet<>(mappedToString);
        return Stream.concat(firstItem, set.stream()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public Set<String> getReportAccessories() {
        Stream<String> firstItem = Stream.of(REPORT_ACCESSORY_NOT_CHOOSEN);
        List<String> mappedToString = reports.stream()
                .filter(reportEntity -> reportEntity.getAccessory() != null)
                .map(report ->report.getAccessory())
                .collect(Collectors.toList());
        Set<String> set = new HashSet<>(mappedToString);
        log.info("Accessory spinner items " + mappedToString);
        Set<String> test = Stream.concat(firstItem, set.stream()).collect(Collectors.toCollection(LinkedHashSet::new));
        log.info("Accessory spinner items all" + test);
        return test;
    }
    public Set<String> getReportClothes() {
        Stream<String> firstItem = Stream.of(REPORT_CLOTH_NOT_CHOOSEN);
        List<String> mappedToString = reports.stream()
                .filter(reportEntity -> reportEntity.getCloth() != null)
                .map(report ->report.getCloth())
                .collect(Collectors.toList());
        Set<String> set = new HashSet<>(mappedToString);
        return Stream.concat(firstItem, set.stream()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
