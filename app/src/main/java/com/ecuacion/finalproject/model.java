package com.ecuacion.finalproject;

public class model {
    private String title;
    private String message;
    private int hour; // Add fields for hour and minute
    private int minute;
    private int day;
    private int month;
    private int year;

    public model() {

    }

    public model(String title, String message, int hour, int minute, int day, int month, int year) {
        this.title = title;
        this.message = message;
        this.hour = hour;
        this.minute = minute;
        this.day = day;
        this.month = month;
        this.year = year;

    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }


    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
