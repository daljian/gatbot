package se.teddy.atg.race

/**
 * Created by gengdahl on 2015-12-07.
 */
enum ANALYZER {
    INSTANCE;
    private int[] rankedHorsesWins = [0,0,0] //first, second, third
    private int[] topRankedHorsePlace = [0,0,0] //first, second, third
    public void analyzeLoss(Race race){
        feedArrays(race)
    }
    public void analyzeWin(Race race){
        feedArrays(race)
    }

    public void analyzePass(Race race){
        feedArrays(race)
    }
    public void feedArrays(Race race){
        def distanceToWinner = 0
        race.winners.each { winner ->
            if (winner.startNumber == race.rankedHorses[0].startNumber){
                topRankedHorsePlace[distanceToWinner]++
            }
            distanceToWinner++
        }
        def reserveNumber = 0
        race.rankedHorses.subList(0,3).each { horse ->
            if (horse.startNumber == race.winners[0].startNumber){
                rankedHorsesWins[reserveNumber]++
            }
            reserveNumber++
        }
    }
    public String toString(){
        println "Top ranked horse placed: [1,2,3] in races: ${topRankedHorsePlace}"
        println "Ranked horse #[1,2,3] won in races: ${rankedHorsesWins}"
    }



}