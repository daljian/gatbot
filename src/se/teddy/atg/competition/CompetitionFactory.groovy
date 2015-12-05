package se.teddy.atg.competition

/**
 * Created by gengdahl on 2015-11-30.
 */
class CompetitionFactory {
    static Competition createCompetition(def type, Map<String, ?> data){
        switch (type){
            case 'dd': return new DagensDubbel(data); break
            case 'ld': return new LunchDubbel(data); break
            case 'komb': return new Komb(data); break
            case 'plats': return new Plats(data); break
            case 'raket': return new Raket(data); break
            case 'trio': return new Trio(data); break
            case 'tvilling': return new Tvilling(data); break
            case 'V3': return new V3(data); break
            case 'V4': return new V4(data); break
            case 'V5': return new V5(data); break
            case 'V64': return new V64(data); break
            case 'V65': return new V65(data); break
            case 'V75': return new V75(data); break
            case 'V86': return new V86(data); break
            case 'vinnare': return new Vinnare(data); break
            case 'vp': return new VinnareOchPlats(data); break
            default:
                println "WARNING: Found unknown competition (${type}. Will create generic Competition Object";
                return new Competition(type, data){
                    public def getBet(){0}
                    public def placeBet(def bet){0}
                }
                break
        }
    }
}
