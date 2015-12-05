package se.teddy.atg.race

/**
 * Created by gengdahl on 2015-11-30.
 */
enum RACE {
    REPO;
    private final Map <String, Race> races = new HashMap<String,Race>()
    public void put(String key, Race race){
        races.put(key,race);
    }
    public boolean containsKey(String key){
        races.containsKey(key)
    }
    public Race get(String key){
        races.get(key)
    }
    public clear(){
        races.clear()
    }

}