package se.teddy.atg.horse

/**
 * Created by gengdahl on 2015-11-29.
 */
class WinningHorse {
    private int odds;
    private int startNumber
    public WinningHorse(int startNumber, int odds){
        this.startNumber = startNumber;
        this.odds = odds;
    }
    public int getStartNumber(){
        return startNumber;
    }
    public int getOdds(){
        return odds;
    }
    public String toString(){
        "${startNumber} (${odds})"
    }
}
