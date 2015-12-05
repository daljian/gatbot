package se.teddy.atg

import se.teddy.atg.horse.Horse
import se.teddy.atg.race.RACESTATS
import se.teddy.atg.rest.ATGApi
import se.teddy.atg.utils.DATE
import se.teddy.atg.utils.WALLET

/**
 * Created by ia on 2015-11-08.
 */


DATE.INSTANCE.set("2014-12-31")

while(!DATE.INSTANCE.next().equals("2015-12-03")){
    try{
        Map<String,List<Horse>> rankedHorses = ATGApi.getInstance().getRankedHorses(DATE.INSTANCE.toString())

        rankedHorses.each{race, horses ->
            print "DD${race+1}: "
            horses.each { horse ->
                print "[${horse.getDisplayName()}]   "
            }
            println ""
        }
    }catch(Exception ex){
        ex.printStackTrace()
    }
}

println "Evaluated ${RACESTATS.REPO.totalCount} races with win factor ${RACESTATS.REPO.winsCount/RACESTATS.REPO.totalCount}"

println WALLET.INSTANCE
