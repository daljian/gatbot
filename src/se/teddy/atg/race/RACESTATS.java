package se.teddy.atg.race;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gengdahl on 2015-12-02.
 */
public enum RACESTATS {
  REPO;

  private synchronized void initStats() {
    wins.set(0);
    losses.set(0);
    stats.clear();
  }

  public int addWin(String raceID) {
    stats.put(raceID, WIN);
    return wins.addAndGet(1);
  }

  public int addLoss(String raceID) {
    stats.put(raceID, LOSS);
    return losses.addAndGet(1);
  }

  public int addPass(String raceID) {
    stats.put(raceID, PASS);
    return passed.addAndGet(1);
  }

  public int getWinsCount() {
    return wins.get();
  }

  public int getLossesCount() {
    return losses.get();
  }

  public int getPassedCount() {
    return passed.get();
  }

  public int getTotalCount() {
    return stats.size();
  }

  public void clear() {
    initStats();
  }

  /**
   * @return <id,result> where result is one of {WIN,PASS,LOSS}
   */
  public Map<String, String> getStats() {
    return ((Map<String, String>) (stats));
  }

  public void setStats(Map<String, String> stats) {
    this.stats = stats;
  }

  private Map<String, String> stats = new HashMap<String, String>();
  private static final String WIN = "WIN";
  private static final String LOSS = "LOSS";
  private static final String PASS = "PASS";
  private AtomicInteger wins = new AtomicInteger(0);
  private AtomicInteger losses = new AtomicInteger(0);
  private AtomicInteger passed = new AtomicInteger(0);
}
