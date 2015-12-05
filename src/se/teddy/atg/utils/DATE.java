package se.teddy.atg.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * Created by gengdahl on 2015-11-26.
 */
public enum DATE {
    INSTANCE;
    private LocalDate date = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public String previous(){
        date = date.minusDays(1);
        return date.format(formatter);
    }
    public String next(){
        date = date.plusDays(1);
        return date.format(formatter);
    }
    public String toString(){

        return date.format(formatter);
    }
    public void set(String dateString){
        date = LocalDate.parse(dateString, formatter);
    }

}
