package com.example.inzynierka;

class UnknownReportDateException extends Exception {
    public UnknownReportDateException(){
        super("Podany format daty nie jest znany. Poprawny format to DD-MM-YYYY");
    }
}
