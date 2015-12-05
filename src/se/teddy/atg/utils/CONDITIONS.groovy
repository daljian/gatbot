package se.teddy.atg.utils

/**
 * Created by gengdahl on 2015-12-03.
 */
enum CONDITIONS {
    MIN_WIN_MARGIN(50), //Km time compared between 1st & 2nd ranked horse in milliseconds
    MIN_ODDS(2000),
    MAX_ODDS(5000);
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


}