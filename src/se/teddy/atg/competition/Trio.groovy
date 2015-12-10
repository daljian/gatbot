package se.teddy.atg.competition

/**
 * Created by gengdahl on 2015-11-30.
 */
class Trio extends Competition{
    Trio(Map<String, ?> data) {
        super('trio', data)
    }
    /**
     * Evaluate if this competition is bettable
     * and return the suggested amount to bet.
     *
     * @return Suggested amount to bet, can be 0
     */
    @Override
    def getBet() {
        return 0
    }

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
    @Override
    def placeBet(Object bet) {
        return 0
    }
    @Override
    public String getHumanReadableInfo(){"NO INFO"}

}
