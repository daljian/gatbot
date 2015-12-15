package se.teddy.atg.competition

import se.teddy.atg.race.Race
import se.teddy.atg.utils.CONDITIONS
import se.teddy.atg.utils.DATE
import se.teddy.atg.utils.FILTERS
import se.teddy.atg.utils.WALLET

/**
 * Created by gengdahl on 2015-12-07.
 */
class Dubbel extends Competition {

    Dubbel(String type, Map<String, ?> data) {
        super(type, data)
    }
    private def getOddsMatrix(){
        details.pools.get(name).comboOdds
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
            payBack = getBetOdds()*bet
        }
        if (FILTERS.ENABLED_PRINTOUTS_DAILY_BETS.exists(this.getClass().getName())){
            println humanReadableInfo
        }
        payBack
    }




    @Override
    def getBet() {
        def bet = CONDITIONS.BET_FIXED_AMOUNT.get()
        def ddWinDiff = 0
        races.each {race ->
            if (race.isBettable()){
                if (WALLET.INSTANCE.get() > 0){
                    bet += WALLET.INSTANCE.get()*CONDITIONS.BET_VARIABLE_FACTOR.get()
                }
                ddWinDiff += race.getRankedHorses()[1].getAverageKmTime() - race.getRankedHorses()[0].getAverageKmTime()
            }else{
                bet = 0
            }
        }
        if (bet > 0){
            def xIndex = races[0].rankedHorses[0].startNumber -1
            def yIndex = races[1].rankedHorses[0].startNumber -1
            def odds = getOddsMatrix()[xIndex][yIndex]
            if (odds < CONDITIONS.DUBBEL_MIN_WIN_ODDS.get() || odds > CONDITIONS.DUBBEL_MAX_WIN_ODDS.get()){
                bet = 0
            }
            if (ddWinDiff < CONDITIONS.DUBBEL_MIN_WIN_MARGIN.get()){
                bet = 0
            }
        }
        bet
    }
    @Override
    public String getHumanReadableInfo(){
        "${name.toUpperCase()} ${timestamp} ${winLooseMarkers}"
    }

    def getWinLoosePassLabel(){
        def label = "NOT_KNOWN"
        if (getBet() == 0){
            label = "PASSED"
        }else if (races[0].hasWinners() && races[1].hasWinners()){
            if (isWin()){
                label = "WIN"
            }else{
                label = "LOOSE"
            }
        }
        label
    }
    boolean isWin(){
        def win = false
        if (races[0].hasWinners() && races[1].hasWinners()){
            win = races[0].winners[0].startNumber == races[0].rankedHorses[0].startNumber &&
                    races[1].winners[0].startNumber == races[1].rankedHorses[0].startNumber
        }
        win

    }
    private int getBetOdds(){
        def odds = 0
        if (!races[0].cancelled && !races[1].cancelled){
            def xIndex = races[0].rankedHorses[0].startNumber-1
            def yIndex=races[1].rankedHorses[0].startNumber-1
            odds = details.pools.get(name).comboOdds[xIndex][yIndex]
        }
        odds
    }
    private int getWinOdds(){
        def odds = 0
        if (races[0].hasWinners() && races[1].hasWinners()){
            def xIndex = races[0].winners[0].startNumber - 1
            def yIndex=races[1].winners[0].startNumber - 1
            odds = details.pools.get(name).comboOdds[xIndex][yIndex]
        }
        odds
    }
    String getWinLooseMarkers(){
        "${getWinLooseMarker(races[0])} ${getWinLooseMarker(races[1])} Odds: ${getWinOdds()}(${getBetOdds()}) Status: ${getWinLoosePassLabel()}    Wallet: ${WALLET.INSTANCE}"
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

