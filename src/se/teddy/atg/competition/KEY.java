package se.teddy.atg.competition;

/**
 * Created by gengdahl on 2015-12-10.
 */
public enum KEY {
  ID("id"), RACES("races"), START_SCHEDULED("scheduledStartTime"), START_ACTUAL("startTime"), STATUS("status");

  private KEY(String name) {
    this.name = name;
  }

  public String getValue() {
    return name;
  }

  private String name;
}
