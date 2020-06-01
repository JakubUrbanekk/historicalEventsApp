package com.example.inzynierka;

import com.example.inzynierka.CustomDate.CustomData;
import com.example.inzynierka.CustomDate.UnknownReportDateException;
import com.example.inzynierka.Database.Report.ReportEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CustomDataSpec {
    CustomData customData;
    CustomData currentData;
    ReportEntity reportEntity;
    @BeforeEach
    void init (){
        reportEntity = new ReportEntity();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        String date = simpleDateFormat.format(calendar.getTime());
        reportEntity.setReportDate(date);
        customData = new CustomData(reportEntity.getReportDate());
        currentData = new CustomData(simpleDateFormat.format(new Date()));
    }
    @Test
    void isMonthValueEqualsToTwelveIfDateStringHaveTwelveMonthValue(){
        assertEquals("Data pobiera nieprawidłowy dzień z daty", customData.getDay(), Integer.toString(12));
    }
    @Test
    void isDateSplitIntoSubstringCorrectly(){
        List<String> result = customData.convertStringDateToStringList(reportEntity.getReportDate());
        List<String> expected = Arrays.asList(12+"",11+"", 2019+"");
        assertThat("Przekonwertowana data jest nieprawidlowa", result, is(expected));
    }
    @Test
    void isWrongDataFormatExceptionCatched(){
        Assertions.assertThrows(UnknownReportDateException.class, () -> customData.checkIfDateFormatIsCorrect("12-21/563"));
    }
    @Test
    void isCorrectDateFormatAccepted() {
        try {
            assertTrue(customData.checkIfDateFormatIsCorrect("10-02-2012"));
        } catch (UnknownReportDateException e) {
            e.printStackTrace();
        }
    }
    @Test
    void getCorrectDataSeparator(){
        String result = customData.getDataSeparatorFromDateString("12-10-2019");
        assertThat("Pobrany separator jest nieprawidlowy", result, is("-"));
    }
    @Test
    void getDateFromPastYear(){
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 22);
        String date = simpleDateFormat.format(calendar.getTime());
        CustomData customDataToCompare = new CustomData(date);

        assertTrue("Zwrocono date pozniejsza niz rok temu", CustomData.checkIfDateIsFromPastYears(1, customDataToCompare));
    }
    @Test
    void getDateFromPastTwoMonths(){
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        String date = simpleDateFormat.format(calendar.getTime());
        CustomData customDataToCompare = new CustomData(date);
        assertTrue("Zwrocono date pozniejsza niz miesiac temu", CustomData.checkIfDateIsFromPastMonths(2, customDataToCompare));
    }
    @Test
    void dateNotFromPastTwoMonths(){
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        String date = simpleDateFormat.format(calendar.getTime());
        CustomData customDataToCompare = new CustomData(date);
        assertFalse("Zwrocono date pozniejsza niz miesiac temu", CustomData.checkIfDateIsFromPastMonths(2, customDataToCompare));
    }
    @Test
    void dateFromPastThreeMonths(){
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        String date = simpleDateFormat.format(calendar.getTime());
        CustomData customDataToCompare = new CustomData(date);
        assertTrue("Zwrocono date pozniejsza niz 3 miesiace temu", CustomData.checkIfDateIsFromPastMonths(3, customDataToCompare));
    }
    @Test
    void dateFromPreviousWeek() {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 20);
        String date = simpleDateFormat.format(calendar.getTime());
        CustomData customDataToCompare = new CustomData(date);
        assertTrue("Zwrocono date pozniejsza niz miesiac temu", CustomData.checkIfDateIsFromPastDays(2, customDataToCompare));
    }
}
