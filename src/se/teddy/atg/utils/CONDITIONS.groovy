package se.teddy.atg.utils

/**
 * Created by gengdahl on 2015-12-03.
 */
enum CONDITIONS {
    BET_FIXED_AMOUNT(100),
    BET_VARIABLE_FACTOR(0), //factor of wallet amount; if wallet is negative variable part of bet is zero
    MIN_WIN_MARGIN(0), //Km time compared between 1st & 2nd ranked horse in milliseconds
    DUBBEL_MIN_WIN_MARGIN(0), //km time diff on both races combined
    DUBBEL_MIN_WIN_ODDS(0), //Min combined odds on Dubbel competition
    DUBBEL_MAX_WIN_ODDS(Integer.MAX_VALUE), //Max combined odds on Dubbel competition
    MIN_ODDS(1), //Min odds on a single race. TODO, howto get this in a Competition agnostic way. Race only has actual odds after race is complete...
    MAX_ODDS(Integer.MAX_VALUE); //Max odds on a single race
    private def value;
    public CONDITIONS(def value){
        this.value = value;
    }
    public def get(){
        value
    }
    public void set(def value){
        this.value = value
    }
    public String toString(){
        return "${value}"
    }


}