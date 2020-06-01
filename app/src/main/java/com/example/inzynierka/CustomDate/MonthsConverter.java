package com.example.inzynierka.CustomDate;

import java.util.Arrays;
import java.util.List;

public class MonthsConverter {
    static List<String> months = Arrays.asList("Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień"
            ,"Wrzesień", "Październik", "Listopad", "Grudzień");

    public static int convertMonthToNumber(String monthToCompare) throws UnknownMonthException {
        int monthNumber = months.indexOf(monthToCompare);
        if (monthNumber != -1){
            return monthNumber+1;
        }
        else throw new UnknownMonthException("Podany miesiąć nie istnieje. Poprawne nazwy miesięcy to: Styczeń\", \"Luty\", \"Marzec\", \"Kwiecień\", \"Maj\", \"Czerwiec\", \"Lipiec\", \"Sierpień\"\n" +
                "            ,\"Wrzesień\", \"Październik\", \"Listopad\", \"Grudzień");
    }


    public static String convertNumberToMonth(int number) throws UnknownMonthException{
        if (number<=0 || number>=13){
            throw new UnknownMonthException("Podany numer miesiaca nie istnieje. Poprawny number jest z zakresu <1,12>");
        }
        return months.get(number-1);
    }
}
