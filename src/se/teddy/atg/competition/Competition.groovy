package se.teddy.atg.competition

import groovy.json.JsonBuilder
import se.teddy.atg.race.RACE
import se.teddy.atg.race.Race
import se.teddy.atg.rest.ATGApi
import se.teddy.atg.utils.FILTERS

/**
 * Created by gengdahl on 2015-11-29.
 */
abstract class Competition {
    enum KEY{
        ID('id'),
        RACES('races'),
        START_SCHEDULED('scheduledStartTime'),
        START_ACTUAL('startTime'),
        STATUS('status'),
        TRACKS('82');

        private String name;
        private KEY(String name){
            this.name = name;
        }
        String getValue(){
            return name;
        }
    }
    String name;
    Map<String, ?> overview
    Map <String, ?> details
    def id
    public Competition(def name, Map<String, ?> data){
        this.name = name;
        this.overview = data;
        this.id = data[KEY.ID.value];

    }
    public void populate(){
        this.details = ATGApi.instance.getCompetitionData(id)

    }
    public List<Race> getRaces(){
        List<String> raceIDs = overview.get(KEY.RACES.value)
        List<Race> races = new ArrayList<Race>(raceIDs.size())
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
    /**
     * Evaluate if this competition is bettable
     * and return the suggested amount to bet.
     *
     * @return Suggested amount to bet, can be 0
     */
    public abstract def getBet();

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
