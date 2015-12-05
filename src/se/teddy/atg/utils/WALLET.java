package se.teddy.atg.utils;

/**
 * Created by gengdahl on 2015-11-26.
 */
public enum WALLET {
    INSTANCE;
    private double amount = 0;
    private double min = 0;
    private double max = 0;
    public void add(double value){
        amount += value;
        if (amount > max){
            max = amount;
        }
    }
    public void remove(double value){
        amount -= value;
        if (amount < min){
            min = amount;
        }

    }
    public void reset(){
        this.amount = 0;

    }
    public double get(){
        return amount;

    }
    public String toString(){
        return amount +" SEK, MIN: " + min + " MAX: " + max;
    }
}
