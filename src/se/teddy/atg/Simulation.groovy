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
import se.teddy.atg.race.ANALYZER
import se.teddy.atg.race.RACE
import se.teddy.atg.utils.CONDITIONS
import se.teddy.atg.race.RACESTATS
import se.teddy.atg.race.RaceDay
import se.teddy.atg.rest.ATGApi
import se.teddy.atg.utils.DATE
import se.teddy.atg.utils.FILTERS
import se.teddy.atg.utils.WALLET

import java.time.DayOfWeek
import java.time.LocalDateTime
import static java.time.DayOfWeek.MONDAY
import static java.time.DayOfWeek.TUESDAY
import static java.time.DayOfWeek.WEDNESDAY
import static java.time.DayOfWeek.THURSDAY
import static java.time.DayOfWeek.FRIDAY
import static java.time.DayOfWeek.SATURDAY
import static java.time.DayOfWeek.SUNDAY

/**
 * Created by ia on 2015-11-08.
 */


// Setup filters to use;
// A filter is used to exclude or include something
//ALL: FILTERS.ENABLED_COMPETITION_TYPES.add(Komb.getName()).add(Plats.getName()).add(Raket.getName()).add(Trio.getName()).add(Tvilling.getName()).add(V3.getName()).add(V4.getName()).add(V5.getName()).add(V64.getName()).add(V65.getName()).add(V86.getName()).add(Vinnare.getName()).add(VinnareOchPlats.getName())
//FILTERS.ENABLED_COMPETITION_TYPES.add(V3.getName()).add(V5.getName()).add(V64.getName()).add(V65.getName()).add(V86.getName()).add(V75.getName()).add(DagensDubbel.getName())
FILTERS.ENABLED_COMPETITION_TYPES.add(DagensDubbel.getName()).add(LunchDubbel.getName())
FILTERS.ENABLED_WEEK_DAYS.add(MONDAY).add(TUESDAY).add(WEDNESDAY).add(THURSDAY).add(FRIDAY).add(SATURDAY).add(SUNDAY)

//Print info about every bet placed
//FILTERS.ENABLED_PRINTOUTS_DAILY_BETS.add(DagensDubbel.getName()).add(LunchDubbel.getName())

// Setup conditions to use
// conditions are used to evaluate how/if/how much to bet on a race
CONDITIONS.MIN_WIN_MARGIN.set(0)
CONDITIONS.DUBBEL_MIN_WIN_MARGIN.set(0)
CONDITIONS.DUBBEL_MAX_WIN_ODDS.set(30000)
CONDITIONS.DUBBEL_MIN_WIN_ODDS.set(1)
CONDITIONS.BET_FIXED_AMOUNT.set(100)
CONDITIONS.BET_VARIABLE_FACTOR.set(1/5000)


//Preset intervals:
def TEST_INTERVAL_ALL_TIME = ['2011-12-31', DATE.INSTANCE.reset().next().toString()]
def TEST_INTERVAL_2012 = ['2011-12-31','2013-01-01']
def TEST_INTERVAL_2013 = ['2012-12-31','2014-01-01']
def TEST_INTERVAL_2014 = ['2013-12-31','2015-01-01']
def TEST_INTERVAL_CURRENT_YEAR = ['2014-12-31', DATE.INSTANCE.reset().next().toString()]
def TEST_INTERVAL_YESTERDAY = [DATE.INSTANCE.reset().previous().previous().toString(), DATE.INSTANCE.reset().toString()]
def TEST_INTERVAL_TODAY = [DATE.INSTANCE.reset().previous().toString(), DATE.INSTANCE.reset().next().toString()]




//Currently used interval
def TEST_INTERVAL_NON_INCLUSIVE= TEST_INTERVAL_ALL_TIME

println "Running over date (start/end dates not included): ${TEST_INTERVAL_NON_INCLUSIVE}"

//Main program..
DATE.INSTANCE.set(TEST_INTERVAL_NON_INCLUSIVE[0])
def count = 0
while(!DATE.INSTANCE.next().toString().equals(TEST_INTERVAL_NON_INCLUSIVE[1])){
    try{
        if (FILTERS.ENABLED_WEEK_DAYS.exists(DATE.INSTANCE.getLocalDate().dayOfWeek)){
            def raceDay = new RaceDay(ATGApi.getInstance().getJson("calendar/day/${DATE.INSTANCE.toString()}", null))
            raceDay.placeBets(false)
        }else{
            println "Skipping ${DATE.INSTANCE}"
        }
    }catch(Exception ex){
        println "Caught Exception on date ${DATE.INSTANCE}"
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


//Analyze section, what went wrong, what was good, how to improve?
RACESTATS.REPO.stats.each {id, result ->
    def race = RACE.REPO.get(id)
    switch (result){
        case 'WIN': ANALYZER.INSTANCE.analyzeWin(race);break;
        case 'LOSS': ANALYZER.INSTANCE.analyzeLoss(race); break;
        case 'PASS': ANALYZER.INSTANCE.analyzePass(race); break;
    }
}
println ANALYZER.INSTANCE
