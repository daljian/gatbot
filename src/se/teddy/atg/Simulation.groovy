package se.teddy.atg

import se.teddy.atg.competition.DagensDubbel
import se.teddy.atg.competition.Komb
import se.teddy.atg.competition.LunchDubbel
import se.teddy.atg.competition.Plats
import se.teddy.atg.competition.Raket
import se.teddy.atg.competition.Trio
import se.teddy.atg.competition.Tvilling
import se.teddy.atg.competition.V3
import se.teddy.atg.competition.V4
import se.teddy.atg.competition.V5
import se.teddy.atg.competition.V64
import se.teddy.atg.competition.V65
import se.teddy.atg.competition.V75
import se.teddy.atg.competition.V86
import se.teddy.atg.competition.Vinnare
import se.teddy.atg.competition.VinnareOchPlats
import se.teddy.atg.utils.CONDITIONS
import se.teddy.atg.race.RACESTATS
import se.teddy.atg.race.RaceDay
import se.teddy.atg.rest.ATGApi
import se.teddy.atg.utils.DATE
import se.teddy.atg.utils.FILTERS
import se.teddy.atg.utils.WALLET

/**
 * Created by ia on 2015-11-08.
 */


// Setup filters to use;
// A filter is used to exclude or include something
//ALL: FILTERS.ENABLED_COMPETITION_TYPES.add(Komb.getName()).add(Plats.getName()).add(Raket.getName()).add(Trio.getName()).add(Tvilling.getName()).add(V3.getName()).add(V4.getName()).add(V5.getName()).add(V64.getName()).add(V65.getName()).add(V86.getName()).add(Vinnare.getName()).add(VinnareOchPlats.getName())
FILTERS.ENABLED_COMPETITION_TYPES.add(V3.getName()).add(V5.getName()).add(V64.getName()).add(V65.getName()).add(V86.getName()).add(V75.getName()).add(DagensDubbel.getName())
//FILTERS.ENABLED_COMPETITION_TYPES.add(DagensDubbel.getName())

// Setup conditions to use
// conditions are used to evaluate how/if/how much to bet on a race
CONDITIONS.MIN_WIN_MARGIN.set(10)
CONDITIONS.MAX_ODDS.set(9999)
CONDITIONS.MIN_ODDS.set(3)

//Main program..
DATE.INSTANCE.set("2015-01-01")
def count = 0
while(!DATE.INSTANCE.next().equals("2015-12-03")){
    try{
        print "."
        def raceDay = new RaceDay(ATGApi.getInstance().getJson("calendar/day/${DATE.INSTANCE.toString()}", null))
        raceDay.placeBets(false)
    }catch(Exception ex){
        ex.printStackTrace()
    }
    if (DATE.INSTANCE.toString().endsWith("-01")){
        if (RACESTATS.REPO.totalCount != RACESTATS.REPO.passedCount){
            println "\n${DATE.INSTANCE} - Evaluated ${RACESTATS.REPO.totalCount} races with win factor ${RACESTATS.REPO.winsCount/(RACESTATS.REPO.totalCount - RACESTATS.REPO.passedCount)}.  Wins: ${RACESTATS.REPO.winsCount} Passed: ${RACESTATS.REPO.passedCount} Losses: ${RACESTATS.REPO.lossesCount} Wallet: ${WALLET.INSTANCE}"
        }else{
            println "\nAll races were passed..."
        }
    }
}
if (RACESTATS.REPO.totalCount != RACESTATS.REPO.passedCount){
    println "${DATE.INSTANCE} - Evaluated ${RACESTATS.REPO.totalCount} races with win factor ${RACESTATS.REPO.winsCount/(RACESTATS.REPO.totalCount - RACESTATS.REPO.passedCount)}.  Wins: ${RACESTATS.REPO.winsCount} Passed: ${RACESTATS.REPO.passedCount} Losses: ${RACESTATS.REPO.lossesCount} Wallet: ${WALLET.INSTANCE}"
}else{
    println "All races were passed..."
}
println WALLET.INSTANCE
