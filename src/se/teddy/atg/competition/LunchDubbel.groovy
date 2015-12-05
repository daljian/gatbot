package se.teddy.atg.competition

import se.teddy.atg.race.Race
import se.teddy.atg.utils.CONDITIONS
import se.teddy.atg.utils.DATE
import se.teddy.atg.utils.WALLET

/**
 * Created by gengdahl on 2015-11-30.
 */
class LunchDubbel extends Competition{
    LunchDubbel(Map<String, ?> data) {
        super('ld', data)
    }

    /**
     * Evaluate if this competition is bettable
     * and return the suggested amount to bet.
     *
     * @return Suggested amount to bet, can be 0
     */
    @Override
    def getBet() {
        println "LD ${DATE.INSTANCE} ${winLooseMarkers}"
        def bet = 100
        races.each {race ->
            if (!race.isBettable()){
                bet = 0
                //println " race ${race.id} is not bettable"
            }
        }
        if (bet > 0){
            def oddsMatrix =  details.pools.ld.comboOdds
            def xIndex = races[0].rankedHorses[0].startNumber -1
            def yIndex = races[1].rankedHorses[0].startNumber -1
            def odds = oddsMatrix[xIndex][yIndex]
            if (odds/100 < CONDITIONS.MIN_ODDS.get() || odds/100 > CONDITIONS.MAX_ODDS.get()){
                //println " odds are not bettable"
                bet = 0
            }
        }
        bet
    }

    /**
     * If running simulation, you can place a bet on
     * this competition. You will get the payback on your bet.
     *
     * Note! To calculate the net payback, the invoker of this method must
     * withdraw the bet from the payback, Example:
     * if you bet 100 and you get 500 back; your net is (500-100)=400
     *
     * @return the payback, can be zero. Most of the time it's probalby zero ;)
     */
    @Override
    def placeBet(def bet) {
        def payBack = 0
        if (isWin()){
            payBack = getOdds()*bet
        }
        payBack
    }
    boolean isWin(){
        def win = false
        if (races[0].hasWinners() && races[1].hasWinners()){
            win = races[0].winners[0].startNumber == races[0].rankedHorses[0].startNumber &&
                    races[1].winners[0].startNumber == races[1].rankedHorses[0].startNumber
        }
        win

    }
    private double getOdds(){
        def odds = 0
        if (races[0].bettable && races[1].bettable){
            odds =details.pools.ld.comboOdds[races[0].rankedHorses[0].startNumber-1][races[1].rankedHorses[0].startNumber-1]/100
        }
        odds
    }
    String getWinLooseMarkers(){
        "${getWinLooseMarker(races[0])} ${getWinLooseMarker(races[1])} Odds: ${getOdds()} Wallet (before betting): ${WALLET.INSTANCE}"
    }
    private String getWinLooseMarker(Race race){
        def actualWinner = "?"
        def bettedWinner = "?"
        if (race.hasWinners()){
            actualWinner = race.winners[0].startNumber
        }
        if (!race.rankedHorses.empty){
            bettedWinner = race.rankedHorses[0].startNumber
        }
        "[${actualWinner}(${bettedWinner})]"
    }
}
