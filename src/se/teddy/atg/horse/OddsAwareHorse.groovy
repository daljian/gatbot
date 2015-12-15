package se.teddy.atg.horse

/**
 * Created by gengdahl on 2015-12-07.
 */
class OddsAwareHorse extends Horse {
    public OddsAwareHorse (def startNumber, def id, def name, def dateString,
                            int upcomingRaceDistance, int odds){
        super(startNumber, id, name, dateString, upcomingRaceDistance)
        this.odds = odds
    }
    private int odds

    @Override
    int compareTo(Object o) {
        def totalDiff = 0;
        if (o instanceof OddsAwareHorse){
            OddsAwareHorse other = (OddsAwareHorse)o
            totalDiff = (this.averageKmTime + odds/60) - (other.averageKmTime + other.odds/60)
        }
        return totalDiff
    }




}
