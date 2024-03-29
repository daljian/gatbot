package se.teddy.atg.race

import groovy.json.JsonBuilder
import se.teddy.atg.competition.Competition
import se.teddy.atg.competition.CompetitionFactory
import se.teddy.atg.utils.WALLET

/**
 * Created by gengdahl on 2015-11-29.
 */
class RaceDay {
    public RaceDay(Map data){
        data.games.each{ name, competitions ->
            competitions.each{ competition ->
                def concreteCompetition = CompetitionFactory.createCompetition(name, competition)
                if (concreteCompetition.enabled){
                    concreteCompetition.populate()
                    this.competitions.put(name, concreteCompetition)
                    concreteCompetition.races.each {race ->
                        races.add(race)
                    }
                }
            }
        }
    }

    public List<Race> getRaces(){
        new ArrayList<Race>(races);
    }
    public def placeBets(boolean verboseOutput){
        competitions.each {name, competition ->
            if (verboseOutput){
                println "Competition ${competition}"
            }
            def bet = competition.bet
            //Always place bet, even if it's zero
            WALLET.INSTANCE.remove(bet)
            def payback = competition.placeBet(bet/100)
            WALLET.INSTANCE.add(payback)
        }

    }
    public List<Competition> listCompetitions(){
        List<Competition> list = competitions.values().toList()
        Collections.sort(list)
        list
    }


    private Map<String, Competition> competitions = new HashMap<String, Competition>()
    private Set<Race> races = new HashSet<Race>(0)

}
