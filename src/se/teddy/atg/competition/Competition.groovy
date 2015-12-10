package se.teddy.atg.competition

import groovy.json.JsonBuilder
import se.teddy.atg.race.RACE
import se.teddy.atg.race.Race
import se.teddy.atg.rest.ATGApi
import se.teddy.atg.utils.DATE
import se.teddy.atg.utils.FILTERS

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Created by gengdahl on 2015-11-29.
 */
abstract class Competition implements Comparable{

    String name;
    Map<String, ?> overview
    Map <String, ?> details
    String id
    public Competition(String name, Map<String, ?> data){
        this.name = name;
        this.overview = data;
        this.id = data[KEY.ID.value];
        if (id == null){
            new JsonBuilder(data).toPrettyString()
        }

    }
    public def getName(){
        return name
    }

    public void populate(){
        this.details = ATGApi.instance.getCompetitionData(id)
    }
    public List<Race> getRaces(){
        List<String> raceIDs = overview.get(KEY.RACES.value)
        List<Race> races = new ArrayList<Race>(raceIDs.size())
        def raceNumber = 0;
        details.races.each{ race ->
            if (!RACE.REPO.containsKey(race.id)){
                RACE.REPO.put(race.id, new Race(race))
            }
            races.add(RACE.REPO.get(race.id))
        }
        races
    }
    public boolean isEnabled(){
        return FILTERS.ENABLED_COMPETITION_TYPES.exists(getClass().getName())
    }
    public String getTimestamp(){
        return overview[KEY.START_SCHEDULED.value]
    }
    public long getStartTimeMillis(){
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    /**
     * Compare by competition start time; past,now,future
     * @param o
     * @return
     */
    @Override
    int compareTo(Object o) {
        Competition other = (Competition)o
        LocalDateTime myDateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime yourDateTime = LocalDateTime.parse(other.timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return myDateTime.compareTo(yourDateTime);
    }
        /**
     * Evaluate if this competition is bettable
     * and return the suggested amount to bet.
     *
     * @return Suggested amount to bet, can be 0
     */
    public abstract def getBet();
    /**
     * Get human readable information about competition.
     * If competition is in future, the current suggested
     * bet is returned.
     *
     * If competition is in the passed, the result will also
     * be present.
     * @return Time adjusted human readable information about competition.
     */
    public abstract String getHumanReadableInfo();

    /**
     * If running simulation, you can place a bet on
     * this competition. You will get the payback on your bet.
     *
     * Note! To calculate the net payback, the invoker of this method must
     * withdraw the bet from the payback, Example:
     * if you bet 100 and you get 500 back; your net is (500-100)=400
     *
     * @param The bet to place
     * @return the payback, can be zero. Most of the time it's probalby zero ;)
     */
    public abstract def placeBet(def bet);
    public String toString(){
        return new JsonBuilder(details).toPrettyString()
    }

}
