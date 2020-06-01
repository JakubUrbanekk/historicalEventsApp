package com.example.inzynierka.CustomDate;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class CustomData implements Serializable {
    private static Date currentDate;
    SimpleDateFormat sdf;
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
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = new Date();
    }

    public static boolean checkIfDateIsFromPastMonths(int months, CustomData customDataToCompare) {
        Date localDateToCompare = customDataToCompare.converToLocalDate();

        long diff = Math.abs(currentDate.getTime() - localDateToCompare.getTime());
        long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        long pastMonths = diffDays/30;

        if (pastMonths <= months){
            return true;
        }
        return false;
    }

    public static boolean checkIfDateIsFromPastYears(int years, CustomData customDataToCompare) {
        Date localDateToCompare = customDataToCompare.converToLocalDate();

        long diff = Math.abs(currentDate.getTime() - localDateToCompare.getTime());
        long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        long pastYears = diffDays/365;
        System.out.println("Diff Days " + diffDays + "Diff years " + pastYears);
        if (pastYears <= years){
            return true;
        }
        return false;
    }

    public static boolean checkIfDateIsFromPastDays(int days, CustomData customDataToCompare) {
        Date localDateToCompare = customDataToCompare.converToLocalDate();
        long diff = Math.abs(currentDate.getTime() - localDateToCompare.getTime());
        long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        long pastDays = diffDays;
        if (pastDays <= days){
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

    public List<String> convertStringDateToStringList(String reportDate) {
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
    public Date converToLocalDate() {
        String dateSeparator = getDataSeparatorFromDateString(this.toString());
        String dateString = day+dateSeparator+month+dateSeparator+year;
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
