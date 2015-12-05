package se.teddy.atg.rest
/**
 * Created by ia on 2015-11-08.
 */
import groovyx.net.http.RESTClient
import se.teddy.atg.horse.Horse
import se.teddy.atg.race.RACESTATS
import se.teddy.atg.utils.WALLET

import static groovyx.net.http.ContentType.JSON

class ATGApi {
    static ATGApi INSTANCE = new ATGApi();
    private ATGApi(){}

    static ATGApi getInstance(){
        INSTANCE
    }

    static def client = new RESTClient( 'https://www.atg.se/services/v1/' )



    static def cache = new Cache('../../../../cache/atg/services/v1')
    public String getCurrentOdds(String dateString, boolean headers){
        String line = ""
        getDDIds("calendar/day/${dateString}").each {
            def firstRaceIndex = 1;
            getDDData("games/${it}").pools.dd.comboOdds.each{
                def secondRaceIndex = 1;
                it.each { current ->
                    if (headers){

                        line += "${firstRaceIndex}-${secondRaceIndex++}"

                    }else{
                        line += "${current}"
                    }
                    line +=","
                }
                firstRaceIndex++
            }
        }
        line.substring(0,line.length() -1)
    }
    public Map<String,List<Horse>> getRankedHorses(String dateString){
        Map<String,List<Horse>> lineUp = new HashMap<String,List<Horse>>()
        getDDIds("calendar/day/${dateString}").each {
            if (it == null){
                return new HashMap()

            }
            Map json = getDDData("games/${it}")
            if ("cancelled".equals(json.pools.dd.status)){
                return new HashMap()
            }

            def ddWinners = json.pools.dd.result.winners.combination[0]
            Map <Integer, List<Integer>> ddWinnerCandidates = new HashMap<Integer, List<Integer>>()
            int odds = json.pools.dd.result.winners.odds[0]
            int ddindex = 0;
            json.races.each{ race ->
                def winner = race.pools.dd.result.winners[0]
                if (winner != ddWinners[ddindex]){
                    println "winner diff!!!! ${winner} --- ${ddWinners[ddindex-1]}"
                    System.exit(-1);

                }
                def scratchings = race.result.scratchings
                if (scratchings == null){
                    scratchings = []
                }
                println ""
                lineUp.put(ddindex, new LinkedList<Horse>())
                def startNumber = 1;
                race.starts.each{start ->
                    if (!scratchings.contains(startNumber)){
                        lineUp.get(ddindex).add(new Horse(startNumber, start.horse.id,start.horse.name, dateString, race.distance))
                    }
                    startNumber++
                }
                Collections.sort(lineUp.get(ddindex))

                lineUp.put(ddindex, lineUp.get(ddindex).subList(0,4))
                ddindex++

            }

            def dd1Diff  = lineUp.get(0).get(1).averageKmTime - lineUp.get(0).get(0).averageKmTime
            def dd2Diff  = lineUp.get(1).get(1).averageKmTime - lineUp.get(1).get(0).averageKmTime

            if (odds < 200) {
                println "NO GAME - BAD ODDS (Wallet: ${WALLET.INSTANCE})"
            } else if (dd1Diff + dd2Diff < 50){
                println "NO GAME - TOO EVEN (Wallet: ${WALLET.INSTANCE})"
            }
            def enoughHorseData = true;
            lineUp.each {race, horses ->
                horses.each { horse ->
                    if (!horse.hasAverageTime()){
                        enoughHorseData = false;
                    }
                }
                lineUp.put(race, horses.subList(0,2))
                if (dd1Diff < 1){
                    ddWinnerCandidates[0] = []
                    lineUp.get(0).each {horse ->
                        ddWinnerCandidates[0] += horse.startNumber
                    }
                }else{
                    ddWinnerCandidates[0] = [lineUp.get(0).get(0).startNumber]
                }
                if (dd2Diff < 1){
                    ddWinnerCandidates[1] = []
                    lineUp.get(0).each {horse ->
                        ddWinnerCandidates[1] += horse.startNumber
                    }
                }else{
                    ddWinnerCandidates[1] = [lineUp.get(1).get(0).startNumber]
                }
            }
            def oddsDivider = ddWinnerCandidates[0].size() * ddWinnerCandidates[1].size()
            if (!enoughHorseData) {
                println "NO GAME - NOT ENOUGH HORSE DATA (Wallet: ${WALLET.INSTANCE})"
            }else{
                int bet = 0
                if (bet < 0){
                    bet = 100
                }else{
                    bet += 100
                }
                WALLET.INSTANCE.remove(bet)
                def dd1win = ddWinnerCandidates[0].contains(ddWinners[0]);
                def dd2win = ddWinnerCandidates[1].contains(ddWinners[1])
                if (dd1win){
                    println "RACE-WIN diff: ${dd1Diff}"
                    RACESTATS.REPO.addWin("wer")
                }else{
                    RACESTATS.REPO.addLoss("wer")
                }

                if (dd2win){
                    RACESTATS.REPO.addWin("wer")
                    println "RACE-WIN diff: ${dd2Diff}"
                }else{
                    RACESTATS.REPO.addLoss("wer")
                }
                if (dd1win & dd2win){

                    WALLET.INSTANCE.add((bet/100) * odds / oddsDivider)
                    println "WIN ${(bet/100)*odds/oddsDivider-100} SEK (Wallet: ${WALLET.INSTANCE})  dd1Diff:    dd2Diff: ${lineUp.get(1).get(1).averageKmTime - lineUp.get(1).get(0).averageKmTime}"
                }else{
                    println "LOOSE ${bet} SEK (Wallet: ${WALLET.INSTANCE})   dd1Diff: ${lineUp.get(0).get(1).averageKmTime - lineUp.get(0).get(0).averageKmTime}   dd2Diff: ${lineUp.get(1).get(1).averageKmTime - lineUp.get(1).get(0).averageKmTime}"
                }
            }
            println "Race gave odds :${odds} on combination: ${ddWinners} on ${dateString}"
        }
        lineUp


    }
    public List<String> getDDIds(String path) {
        getJson(path, null).games.dd.collect {it.id}
    }
    public Map getCompetitionData(String id){
        getJson("games/${id}", null)
    }
    public Map getDDData(String path){
        getJson(path, null)
    }
    public static Map getJson(String path, Map parameters){
        client.setProxy("emea-proxy.uk.oracle.com", 80, "http")
        client.getClient().params.setParameter("http.connection.timeout", new Integer(700))
        client.getClient().params.setParameter("http.socket.timeout", new Integer(700))

        def map
        def cachePath = path
        if (parameters != null){
            cachePath += "?"
            parameters.each {key, value ->
                cachePath += "${key}=${value}&"
            }
            cachePath -= "&"
        }
        if (cache.exists(cachePath)){
            map = cache.get(cachePath)
        }else{
            //print "HTTP GET ${client.getUri()}${cachePath}"
/*
            try{
                synchronized(this){
                    this.wait(250);
                }
            }catch(Exception ex){

            }
            */
            try{
                def resp = client.get(path: path, contentType: JSON, query:parameters)
                assert resp.status == 200  // HTTP response code; 404 means not found, etc.
                cache.put(cachePath, resp.data)
                map = resp.data
            }catch(Exception ex){
                map = new HashMap()
            }
        }
        map
    }
}
