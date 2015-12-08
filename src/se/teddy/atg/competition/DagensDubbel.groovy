package se.teddy.atg.competition

import groovy.json.JsonBuilder
import se.teddy.atg.race.Race
import se.teddy.atg.utils.CONDITIONS
import se.teddy.atg.utils.DATE
import se.teddy.atg.utils.WALLET

/**
 * Created by gengdahl on 2015-11-30.
 */
class DagensDubbel extends Dubbel{
    DagensDubbel(Map<String, ?> data) {
        super('dd', data)
    }
}
