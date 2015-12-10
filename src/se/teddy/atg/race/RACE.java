package se.teddy.atg.race;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gengdahl on 2015-11-30.
 */
public enum RACE {
  REPO;

  public void put(String key, Race race) {
    races.put(key, race);
  }

  public boolean containsKey(String key) {
    return races.containsKey(key);
  }

  public Race get(String key) {
    return races.get(key);
  }

  public void clear() {
    races.clear();
  }

  private final Map<String, Race> races = new HashMap<String, Race>();
}
