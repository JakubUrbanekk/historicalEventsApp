package com.example.inzynierka;

class UnknownReportDateException extends Exception {
    public UnknownReportDateException(String errorMessage){
        super(errorMessage);
    }
}
