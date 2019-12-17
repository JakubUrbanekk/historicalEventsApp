package com.example.inzynierka;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CustomData {
    private static LocalDate currentDate;
    String day;
    String month;
    String year;
    public CustomData(String reportDate) {
        try {
            checkIfDateFormatIsCorrect(reportDate);
        } catch (UnknownReportDateException e) {
            e.printStackTrace();
        }
        List<String> dateAsList = null;
        dateAsList = convertStringDateToStringList(reportDate);
        setDay(dateAsList.get(0));
        setMonth(dateAsList.get(1));
        setYear(dateAsList.get(2));
        currentDate = LocalDate.now();
    }

    public static boolean checkIfDateIsFromPastMonths(int months, CustomData customDataToCompare) {
        LocalDate localDateToCompare = customDataToCompare.converToLocalDate();
        long pastMonths = Math.abs(ChronoUnit.MONTHS.between(currentDate, localDateToCompare));
        if (pastMonths < months){
            return true;
        }
        return false;
    }

    public static boolean checkIfDateIsFromPastYears(int years, CustomData customDataToCompare) {
        LocalDate localDateToCompare = customDataToCompare.converToLocalDate();
        long pastYears = Math.abs(ChronoUnit.YEARS.between(currentDate, localDateToCompare));
        if (pastYears < years){
            return true;
        }
        return false;
    }

    public static boolean checkIfDateIsFromPastDays(int days, CustomData customDataToCompare) {
        LocalDate localDateToCompare = customDataToCompare.converToLocalDate();
        long pastDays = Math.abs(ChronoUnit.DAYS.between(currentDate, localDateToCompare));
        if (pastDays < days){
            return true;
        }
        return false;
    }

    private void setMonth(String month) {
        this.month = month;
    }
    private void setYear(String year) {
        this.year = year;
    }
    private void setDay(String day) {
        this.day = day;
    }
    public String getDay() {
        return day;
    }
    public String getMonth() {
        return month;
    }
    public String getYear() {
        return year;
    }

    List<String> convertStringDateToStringList(String reportDate) {
        String dateSeparator = getDataSeparatorFromDateString(reportDate);
        String[] segments = reportDate.split(dateSeparator);
        return Arrays.asList(segments);
    }
    public boolean checkIfDateFormatIsCorrect(String dateString) throws UnknownReportDateException{
        String dateSeparator = getDataSeparatorFromDateString(dateString);
        Pattern correctDateFormat = Pattern.compile("\\d\\d"+ dateSeparator + "\\d\\d" + dateSeparator + "\\d\\d\\d\\d");
        boolean isCorrectFormat = correctDateFormat.matcher(dateString).matches();
        if(isCorrectFormat){
            return isCorrectFormat;
        }
        else {
            throw new UnknownReportDateException();
        }
    }
    public String getDataSeparatorFromDateString(String dateString) {
        return String.valueOf(dateString.charAt(2));
    }
    @Override
    public String toString() {
        return day + "-" + month + "-" + year;
    }
    public LocalDate converToLocalDate() {
        String dateSeparator = getDataSeparatorFromDateString(this.toString());
        LocalDate localDate = LocalDate.parse(year+dateSeparator+month+dateSeparator+day);
        return localDate;
    }
}
