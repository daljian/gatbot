package se.teddy.atg.utils

/**
 * Created by gengdahl on 2015-12-02.
 */
enum FILTERS {
    ENABLED_COMPETITION_TYPES,
    ENABLED_WEEK_DAYS,
    ENABLED_PRINTOUTS_DAILY_BETS;
    private Set <String>keys = new HashSet<String>();
    public FILTERS add(def key){
        keys.add(key);
        this
    }
    public boolean exists(def key){
        return keys.contains(key);
    }

}
