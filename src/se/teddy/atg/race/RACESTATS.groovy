package se.teddy.atg.race

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by gengdahl on 2015-12-02.
 */
enum RACESTATS {
    REPO;
    Map<String, ?> stats = new HashMap<String,?>()
    private static final String WIN = "WIN";
    private static final String LOSS = "LOSS";
    private static final String PASS = "PASS";
    private AtomicInteger wins = new AtomicInteger(0)
    private AtomicInteger losses = new AtomicInteger(0)
    private AtomicInteger passed = new AtomicInteger(0)
    private synchronized void initStats(){
        wins.set(0)
        losses.set(0)
        stats.clear();
    }
    public def addWin(String raceID){
        stats.put(raceID, WIN)
        wins.addAndGet(1)
    }
    public def addLoss(String raceID){
        stats.put(raceID, LOSS)
        losses.addAndGet(1)
    }
    public def addPass(String raceID){
        stats.put(raceID, PASS)
        passed.addAndGet(1)
    }
    public def getWinsCount(){
        wins.get()
    }
    public def getLossesCount(){
        losses.get()
    }
    public def getPassedCount(){
        passed.get()
    }
    public def getTotalCount(){
        stats.size()
    }
    public void clear(){
        initStats();
    }
    /**
     *
     * @return <id,result> where result is one of {WIN,PASS,LOSS}
     */
    public Map<String, String> getStats(){
        return stats;
    }
}