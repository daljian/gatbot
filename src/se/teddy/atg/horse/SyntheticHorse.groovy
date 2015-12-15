package se.teddy.atg.horse

import se.teddy.atg.rest.ATGApi

/**
 * Created by gengdahl on 2015-12-12.
 */
class SyntheticHorse extends OddsAwareHorse {
    String targetStartMethod
    String targetCondition
    int targetDistance

    public SyntheticHorse(def startNumber, def id, def name, def dateString,
                          int upcomingRaceDistance, String startMethod, String condition, int odds) {
        super(startNumber, id, name, dateString, upcomingRaceDistance, odds)
        this.targetStartMethod = startMethod
        this.targetCondition = condition
        this.targetDistance = upcomingRaceDistance

    }

    @Override
    void populate() {
        if (id != null) {
            if (targetStartMethod == null ||
                    targetCondition == null){
                super.populate()

            }else{
                Map jsonData = ATGApi.getJson("horses/${id}/results", ['stopdate': dateString])
                int counter = 0;
                jsonData.records.each { record ->
                    if (record.disqualified != true &&
                            record.kmTime != null &&
                            record.kmTime.code == null) {
                        if (counter < 7) {
                            //This is important, let's try to put some effort on finding
                            //a good estimate on next performance with trending algorithm
                            long currentTime = record.kmTime.minutes * 60 * 1000 +
                                    record.kmTime.seconds * 1000 +
                                    record.kmTime.tenths * 100;
                            int currentDistance = record.start.distance
                            def currentStartMethod = record.race.startMethod
                            def currentCondition = record.track.condition
                            if (!raceTimes.containsKey(targetDistance)) {
                                raceTimes.put(targetDistance, new LinkedList<Long>())
                            }
                            if (currentStartMethod == null ||
                                    currentCondition == null){

                            }else if (currentDistance.intValue() == targetDistance){
                                raceTimes.get(targetDistance).add(transpondKmTime(targetDistance, targetStartMethod,
                                        targetCondition,currentDistance,currentStartMethod, currentCondition, currentTime))
                                counter++
                            }
                        }

                    }

                }
            }
        }

    }
    private long transpondKmTime(int targetDistance, String targetStartMethod, String targetCondition,
                                 int currentDistance, String currentStartMethod, String currenCondition,
                                 long kmTime){
        Map factors = HORSESTATS.REPO.getPreRenderedFactors();
        return kmTime/factors.get(currentDistance+":"+currentStartMethod+":"+currenCondition)*factors.get(targetDistance+":"+targetStartMethod+":"+targetCondition)
    }
}