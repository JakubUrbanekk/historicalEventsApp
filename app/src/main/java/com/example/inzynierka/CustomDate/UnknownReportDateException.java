package com.example.inzynierka.CustomDate;

public class UnknownReportDateException extends Exception {
    public UnknownReportDateException(){
        super("Podany format daty nie jest znany. Poprawny format to DD-MM-YYYY");
    }
}
