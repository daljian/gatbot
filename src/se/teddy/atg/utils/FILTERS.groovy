package se.teddy.atg.utils

/**
 * Created by gengdahl on 2015-12-02.
 */
enum FILTERS {
    ENABLED_COMPETITION_TYPES;
    private Set <String>keys = new HashSet<String>();
    public FILTERS add(String key){
        keys.add(key);
        this
    }
    public boolean exists(String key){
        return keys.contains(key);
    }

}