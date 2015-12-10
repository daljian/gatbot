package se.teddy.atg.race

import se.teddy.atg.horse.Horse
import se.teddy.atg.horse.OddsAwareHorse
import se.teddy.atg.horse.WinningHorse
import se.teddy.atg.utils.CONDITIONS
import se.teddy.atg.utils.DATE

/**
 * Created by gengdahl on 2015-11-28.
 */

class Race {
    public static final String START_AUTO = "START_AUTO"
    public static final String START_LOOP = "START_LOOP"
    private final Map data;
    private def scratchings;
    private List<WinningHorse> winners = new ArrayList<WinningHorse>(3)
    private List<Horse> lineUp = new ArrayList<>()
    private def oddsArray = []
    private boolean cancelled = false;
    /**
     *
     * @param daceData
     */
    public Race(Map raceData){
        data = raceData;
        if (RACE.REPO.containsKey(data.id)){
            throw new IllegalArgumentException("This race ${data.id} has already been evaluated!")
        }else if (data.status.equals("cancelled")) {
            cancelled = true
        }else{
            populate();
            RACE.REPO.put(data.id, this)
        }

    }
    boolean isCancelled(){
        return cancelled
    }
    void setOddsArray(def oddsArray){
        this.oddsArray = oddsArray
    }
    /**
     *
     * @return The Race Id, used as key in RACE.REPO
     */
    public def getId(){
        return data.id;
    }
    public List<Horse> getRankedHorses(){
        lineUp
    }
    /**
     *
     * @return True if race is bettable, false if not
     */
    public boolean isBettable(){
        def enoughHorseData = true;
        def shortLineup = lineUp
        if (lineUp.size() > 4){
            shortLineup = lineUp.subList(0,4)
        }
        shortLineup.each { horse ->
            if (!horse.hasAverageTime()) {
                enoughHorseData = false;
            }

        }
        def lineUpNotEmpty = lineUp.size() > 0
        def minWinMarginOK = true
        if (lineUpNotEmpty){
            minWinMarginOK = (lineUp[1].averageKmTime - lineUp[0].averageKmTime) > CONDITIONS.MIN_WIN_MARGIN.get()
        }
        def bettable = (enoughHorseData &&
                !cancelled &&
                lineUpNotEmpty &&
                minWinMarginOK)
        if (!bettable){
            //println "enoughHorseData: ${enoughHorseData} cancelled: ${cancelled} lineUpNotEmpty: ${lineUpNotEmpty} minWinMarginOK: ${minWinMarginOK}"

        }
        bettable
    }

    /**
     * Most data will be read directly from the map
     * but some data is stored in the race object.
     * That data is set by this method
     */
    private populate(){
        if (hasWinners()){
            def first = data.pools.plats?.result?.winners?.first[0]
            def second = data.pools.plats?.result?.winners?.second[0]
            def third = data.pools.plats?.result?.winners?.third[0]
            if (first != null) {
                winners[0] = new WinningHorse(first.number, first.odds)
                if (second != null) {
                    winners[1] = new WinningHorse(second.number, second.odds)
                    if (third != null) {
                        winners[2] = new WinningHorse(third.number, third.odds)
                    }
                }
            }
        }
        scratchings = data?.result?.scratchings
        if (scratchings == null){
            scratchings = []
        }

        def startNumber = 1;
        data.starts.each{start ->
            if (!scratchings.contains(startNumber)){
                lineUp.add(new OddsAwareHorse(startNumber, start.horse.id,start.horse.name, DATE.INSTANCE.toString(), data.distance, start.pools.vinnare.odds))
            }
            startNumber++
        }
        Collections.sort(lineUp)
        if (hasWinners()){
            if (!isBettable()){
                RACESTATS.REPO.addPass(data.id)
            }else if (winners[0].startNumber == lineUp.get(0).startNumber){
                RACESTATS.REPO.addWin(data.id)
            }else{
                RACESTATS.REPO.addLoss(data.id)
            }
        }else{
            // No stats on this race yet...
        }
    }
    /**
     * If race has at least one winner, tis returns true
     * @return
     */
    public boolean hasWinners(){
        def retVal =  data?.pools?.plats?.result?.winners != null
        retVal
    }

    public List<WinningHorse> getWinners(){
        return winners
    }



}
