package se.teddy.atg.horse

import se.teddy.atg.rest.ATGApi
import se.teddy.atg.utils.TUNING

/**
 * Created by ia on 2015-11-15.
 */
class Horse implements Comparable{
    Map<Integer, List<Long>> raceTimes = new HashMap<Integer, List<Long>>()
    int startNumber;
    String id;
    String name;
    int upComingRaceDistance
    final int NO_TIME = 100000
    //For simulations we want to limit known facts by setting a historical date
    String dateString;
    String json = ""
    /**
     * Positive means trending faste, negative means teending slower
     */
    private Map <Integer, Long> trends = new HashMap<Integer, Long>()
    public Horse(def startNumber, def id, def name, def dateString,
                 int upcomingRaceDistance){
        this.upComingRaceDistance = upcomingRaceDistance
        this.startNumber = startNumber
        this.id = id
        this.name = name
        this.dateString = dateString
        //print "[${id}:${name}]"
    }
    public String getDisplayName(){
        return "${startNumber}. ${name} ${getAverageKmTime()}"
    }
    public int getStartNumber(){
        return startNumber;
    }
    public boolean hasAverageTime(){
        return averageKmTime != NO_TIME
    }
    public Map<Integer, Long> getAverageKmTimes(){
        Map<Integer, Long> avgRaceTimes = new HashMap<Integer, Long>()
        raceTimes.each {distance, raceTimes ->
            avgRaceTimes.put(distance,getAverageKmTime(distance))
        }
        return avgRaceTimes
    }
    public long getAverageKmTime(){
        return getAverageKmTime(this.upComingRaceDistance)
    }

    public long getSyntheticAverageKmTime(){
        def averageTime = NO_TIME
        def averageDiff = 0
        raceTimes
    }

    public long getAverageKmTime(def distance){
        def averageTime = NO_TIME
        def averageDiff = 0
        def diffs = []
        if (raceTimes.containsKey(distance)){
            def totalTime = 0;
            def previousRaceTime = 0;
            raceTimes.get(distance).each {raceTime ->
                if (previousRaceTime == 0){
                    previousRaceTime = raceTime
                }else{
                    diffs += previousRaceTime - raceTime;
                    previousRaceTime = raceTime;
                }
                totalTime += raceTime
            }
            if (raceTimes.get(distance).size() != 0){
                averageTime = totalTime / raceTimes.get(distance).size();
                def totalDiff = 0
                diffs.each { diff ->
                    totalDiff += diff
                }
                if (diffs.size() != 0){
                    averageDiff = totalDiff / diffs.size()
                }
            }
            //println "AVG: ${averageTime} AVG-DIFF: ${averageDiff} RaceTimes: ${raceTimes.get(upComingRaceDistance)} Diffs: ${diffs}"
        }
        if(upComingRaceDistance == distance){
            averageTime - (averageDiff/TUNING.DIFF_DIVIDER.doubleValue())
        }else{
            (averageTime - (averageDiff/TUNING.DIFF_DIVIDER.doubleValue()))
        }
    }


    @Override
    int compareTo(Object o) {
        def totalDiff = 0;
        if (o instanceof Horse){
            totalDiff = this.averageKmTime - ((Horse)o).averageKmTime
        }
        return totalDiff
    }

    int compareToMany(Object o) {
        def totalDiff = 0;
        if (o instanceof Horse){
            def myAvgRaceTimes = this.getAverageKmTimes()
            def yourAvgRaceTimes = ((Horse)o).getAverageKmTimes()
            myAvgRaceTimes.each {distance, time ->
                if (yourAvgRaceTimes[distance] != null){
                    totalDiff += yourAvgRaceTimes[distance] - time
                }
            }
        }
        return totalDiff
    }
    void populate(){

        if (id != null){
            Map jsonData = ATGApi.getJson("horses/${id}/results", ['stopdate':dateString])
            jsonData.records.each{ record ->
                if (record.disqualified != true &&
                        record.kmTime != null &&
                        record.kmTime.code == null){
                    long time = record.kmTime.minutes * 60 * 1000+
                            record.kmTime.seconds * 1000 +
                            record.kmTime.tenths * 100;
                    int distance = record.start.distance
                    if (!raceTimes.containsKey(distance)){
                        raceTimes.put(distance, new LinkedList<Long>())
                    }
                    //This is important, let's try to put some effort on finding
                    //a good estimate on next performance with trending algorithm
                    if (raceTimes.get(distance).size() <7){
                        raceTimes.get(distance).add(time)
                    }
                    def startMethod = record.race.startMethod
                    def condition = record.track.condition

                    HORSESTATS.REPO.addPerformance(distance,startMethod, condition, time);
                }

            }
        }

    }
    String toString(){
        return "${startNumber}. ${name}"
    }

}
