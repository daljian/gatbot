package se.teddy.atg.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by gengdahl on 2015-11-26.
 */
public enum WALLET {
    INSTANCE;
    private double amount = 0;
    private double min = 0;
    private double max = 0;
    private double maxBet = 0;
    private double maxWin = 0;
    private List<Integer> bets = new ArrayList<Integer>();
    private List<Integer> wins = new ArrayList<Integer>();
    public void add(double value){
        amount += value;
        if (amount > max){
            max = amount;
        }
        if (value != 0){
            wins.add((int)value);
        }
    }
    public void remove(double value){
        amount -= value;
        if (amount < min){
            min = amount;
        }
        if (value != 0){
            bets.add((int)value);
        }

    }
    public void reset(){
        this.amount = 0;

    }
    public double get(){
        return amount;

    }
    public String toString(){
        Collections.sort(bets);
        Collections.reverse(bets);
        Collections.sort(wins);
        Collections.reverse(wins);
        int betRange = 10;
        int winRange = 10;
        if (bets.size() < 10){
            betRange = bets.size();
        }
        if (wins.size() < 10){
            winRange = wins.size();
        }
        return (int)amount +" SEK, MIN: " + (int)min + " MAX: " + (int)max + " Max Bets: " + bets.subList(0,betRange) + " Max Wins: " + wins.subList(0,winRange);
    }
}
