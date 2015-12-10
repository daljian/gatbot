package se.teddy.atg.utils;

/**
 * Created by gengdahl on 2015-12-03.
 */
public enum CONDITIONS {
  DEPOSIT_MONTHLY_AMOUNT(0),
  BET_FIXED_AMOUNT(100),
  BET_VARIABLE_FACTOR(0),
  MIN_WIN_MARGIN(0),
  DUBBEL_MIN_WIN_MARGIN(0),
  DUBBEL_MIN_WIN_ODDS(0),
  DUBBEL_MAX_WIN_ODDS(Integer.MAX_VALUE),
  MIN_ODDS(1),
  MAX_ODDS(Integer.MAX_VALUE),
  CACHE_ENABLED(Boolean.TRUE);

  CONDITIONS(Object value) {
    this.value = value;
  }

  public Object get() {
    return value;
  }

  public void set(Object value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }

  private Object value;
}
