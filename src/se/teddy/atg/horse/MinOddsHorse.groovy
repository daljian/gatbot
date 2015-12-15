package se.teddy.atg.horse

/**
 * Created by gengdahl on 2015-12-13.
 */
class MinOddsHorse extends Horse {
    public MinOddsHorse (def startNumber, def id, def name, def dateString,
                           int upcomingRaceDistance, int odds){
        super(startNumber, id, name, dateString, upcomingRaceDistance)
        this.odds = odds
    }
    private int odds

    @Override
    int compareTo(Object o) {
        def totalDiff = 0;
        if (o instanceof MinOddsHorse){
            MinOddsHorse other = (MinOddsHorse)o
            totalDiff = odds - other.odds
        }
        return totalDiff
    }


}
