package com.example.inzynierka;

import com.example.inzynierka.CustomDate.MonthsConverter;
import com.example.inzynierka.CustomDate.UnknownMonthException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MonthsConverterSpec {
    @Test
    void convertMonthToNumber(){
        String month = "Wrzesień";
        int result = 0;
        try {
            result = MonthsConverter.convertMonthToNumber(month);
        } catch (UnknownMonthException e) {
            e.printStackTrace();
        }
        int expected = 9;
        assertThat(result, is(expected));
    }
    @Test
    void unknownMonthExceptionThrows(){
        Assertions.assertThrows(UnknownMonthException.class,  () -> MonthsConverter.convertMonthToNumber("asd"));
}
    @Test
    void convertNumberToMonth(){
        int month = 9;
        String result = null;
        try {
            result = MonthsConverter.convertNumberToMonth(month);
        } catch (UnknownMonthException e) {
            e.printStackTrace();
        }
        String expected = "Wrzesień";
        assertThat(result, is(expected));
    }
    @Test
    void unknownMonthExceptionThrowsWrongNumber(){
        Assertions.assertThrows(UnknownMonthException.class,  () -> MonthsConverter.convertNumberToMonth(13));
    }
}
