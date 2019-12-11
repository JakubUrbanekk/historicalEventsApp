package com.example.inzynierka;

class CustomData {
    final String[] acceptedDateFormats;
    final String dateFormat;
    String segments[];
    String reportDate;
    int day;
    int month;
    int year;
    public CustomData(String dateSplitter, String reportDate) {
        acceptedDateFormats = new String[]{"-"};
        dateFormat = dateSplitter;
        this.reportDate = reportDate;
    }

    public void setMonth() {
    }

    public String[] convertStringDateToStringArray(){
     //   Arrays.stream(acceptedDateFormats).forEach(s -> ());
            String segments[] = reportDate.split(dateFormat);
            return segments;
    }
}
