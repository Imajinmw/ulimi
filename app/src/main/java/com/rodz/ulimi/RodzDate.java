package com.rodz.ulimi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RodzDate {
    long left;
    public long timestamp;
    public int day, month, year, hour, minutes, seconds;
    String[] days = new String[] {"Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"};
    public static String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    int[] month_days = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    public RodzDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date date = new Date();
        String dateString = formatter.format(date);

        String[] dchars = dateString.split(" ");

        String[] chars = dchars[0].split("/");
        day = Integer.parseInt(chars[0]);
        month = Integer.parseInt(chars[1]);
        year = Integer.parseInt(chars[2]);

        //do for time
        String[] tchars = dchars[1].split(":");
        hour = Integer.parseInt(tchars[0]);
        minutes = Integer.parseInt(tchars[1]);
        seconds = Integer.parseInt(tchars[2]);
    }

    public RodzDate(String timestamp){
        left = Integer.parseInt(timestamp);
        this.timestamp = left;

        year = 1970;
        boolean can = true;
        while (can){
            int secondsYear = 0;
            if (isLeapYear(year)){
                secondsYear = (24 * 60 * 60) * 366;
            }
            else{
                secondsYear = (24 * 60 * 60) * 365;
            }

            if (left > secondsYear){
                left = left - secondsYear;
                year += 1;
            }
            else{
                can = false;
            }
        }

        left += (24 * 60 * 60) + (3600*2);
        month = 0;
        //year= 2022;

        boolean done = true;
        for (int i = 0; i < month_days.length; i++){
            if (done) {
                int month_secs = month_days[i] * (24 * 60 * 60);
                if (left > month_secs) {
                    left = left - month_secs;
                } else {
                    month = i + 1;
                    done = false;
                }
            }
        }
        //System.out.println(left);
        day = (int)(left / (24 * 60 * 60));

        left = left - (day * (24 * 60 * 60));

        hour = (int)(left/3600);

        left = left - (hour * 3600);

        minutes = (int)(left / 60);

        seconds = (int) (left - (minutes * 60));
    }

    public boolean isLeapYear(int num){
        double res = Double.valueOf(String.valueOf(num)) / 4;
        double num2 = Math.floor(res);
        double dif = res - num2;
        //System.out.println("Out:"+dif+","+res);

        if (dif == 0.0){
            return true;
        }
        else{
            return false;
        }
    }

    public RodzDate(int year, int month, int day){
        this.day = day;
        this.month = month;
        this.year = year;
        hour = 0;
        minutes = 0;
        seconds = 0;
    }

    public RodzDate(int year, int month, int day, int hour, int minutes, int seconds){
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getDay(){
        return  day;
    }

    public int getDayOfMonth(){
        return  day;
    }

    public int getMonth(){
        return  month;
    }

    public int getYear(){
        return  year;
    }

    public int getDayOfYear(){
        int day = 0;

        for (int i = 1; i < month; i++){
            day += month_days[i];
        }

        return day + this.day;
    }

    public String getMonthName(){
        String name = "";
        return months[month-1];
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getTimeStampDay(){
        int stamp = 0;
        stamp = (hour * 3600) + (minutes * 60) + seconds;
        return stamp;
    }

    public long getTimeStamp(){
        long time2 = 0;

        //loop through years
        int year = 1970;
        boolean can = true;
        while (can){
            if (this.year != year) {
                int secondsYear = 0;
                if (isLeapYear(year)) {
                    secondsYear = (24 * 60 * 60) * 366;
                } else {
                    secondsYear = (24 * 60 * 60) * 365;
                }
                time2 += secondsYear;
                year += 1;
            }
            else{
                can = false;
            }
        }
        //loop through months
        for (int i = 0; i < month-1; i++ ){
            time2 += month_days[i] * (24 * 60 * 60);
        }

        //add days
        time2 += (day-1) * (24 * 60 * 60);

        //then hour, minutes and seconds
        time2 += (hour * 3600) + (minutes * 60) + seconds;
        return time2;
    }

    public String getFullDate(){
        return getDay()+" "+getMonthName()+" "+getYear()+", "+getHour()+":"+getMinutes()+":"+getSeconds();
    }
}
