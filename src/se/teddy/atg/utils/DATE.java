package se.teddy.atg.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * Created by gengdahl on 2015-11-26.
 */
public enum DATE {
    INSTANCE;
    private final ThreadLocal<LocalDate> tlDate = new ThreadLocal<LocalDate>() {
        @Override
        protected LocalDate initialValue() {
            return LocalDate.now();
        }
    };
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    public DATE reset(){
        tlDate.set(tlDate.get().now());
        return this;
    }

    public DATE previous(){
        tlDate.set(tlDate.get().minusDays(1));
        return this;
    }
    public DATE next(){
        tlDate.set(tlDate.get().plusDays(1));
        return this;
    }
    public String toString(){

        return tlDate.get().format(formatter);
    }
    public boolean isToday(){
        return LocalDate.now().format(formatter).equals(tlDate.get().toString());
    }
    public boolean isPast(){
        boolean past = false;
        if (!isToday()){
            past = tlDate.get().isBefore(LocalDate.now());
        }
        return past;
    }
    public boolean isFuture(){
        return !isToday() && !isPast();
    }
    //TODO add distanceTo operation that takes string in '2015-12-06'
    // and returns the number of days from current INSTANCE date, so that:
    // 0 - same day
    // < 0 past
    // > 0 future
    public void set(String dateString){

        tlDate.set(LocalDate.parse(dateString, formatter));
    }
    public LocalDate getLocalDate(){
        return tlDate.get();
    }

}
