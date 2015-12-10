package se.teddy.atg.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gengdahl on 2015-12-02.
 */
public enum FILTERS {
  ENABLED_COMPETITION_TYPES, ENABLED_WEEK_DAYS, ENABLED_PRINTOUTS_DAILY_BETS;

  public FILTERS add(Comparable<? extends Comparable<? extends Comparable<?>>> key) {
    keys.add((String) key);
    return this;
  }

  public boolean exists(Comparable<? extends Comparable<? extends Comparable<?>>> key) {
    return keys.contains(key);
  }

  private Set<String> keys = new HashSet<String>();
}
