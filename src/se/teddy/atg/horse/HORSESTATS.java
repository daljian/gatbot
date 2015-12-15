package se.teddy.atg.horse;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.json.internal.LazyMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gengdahl on 2015-12-12.
 */
public enum HORSESTATS {
  REPO;

  //Raw data: Distance  -> Start Method -> Condition -> List of kmTimes
  private Map<Integer,Map<String,Map<String,List<Long>>>> rawData = new HashMap<Integer,Map<String,Map<String,List<Long>>>>();

  void addPerformance(Integer distance, String startMethod, String condition, Long kmTime){
    if (distance == null ||
            startMethod == null ||
            condition == null ||
            kmTime == null){
    }else{
      if (!rawData.containsKey(distance)){
        rawData.put(distance, new HashMap<String,Map<String,List<Long>>>());
      }
      if (!rawData.get(distance).containsKey(startMethod)){
        rawData.get(distance).put(startMethod, new HashMap<String,List<Long>>());
      }
      if (!rawData.get(distance).get(startMethod).containsKey(condition)){
        rawData.get(distance).get(startMethod).put(condition, new ArrayList<Long>());
      }
      rawData.get(distance).get(startMethod).get(condition).add(kmTime);
    }

  }

  /**
   * 2140m with autostart and light conditions has factor 1
   * All the rest combinations (eg; 1640,light,volte) is a factor to use when comparing km times.
   */
  Map generateFactors(){
    Map<String, Long> averageTimes = new HashMap<String, Long>();
    for (Integer distance: rawData.keySet()){
      for (String startMethod: rawData.get(distance).keySet()){
        for (String condition: rawData.get(distance).get(startMethod).keySet()){
          int counter = 0;
          double total = 0;
          for (Long kmTime: rawData.get(distance).get(startMethod).get(condition)){
            total += kmTime;
            counter++;
          }
          averageTimes.put(distance +":" +startMethod+":"+condition, (long)(total/counter));
        }
      }
    }
    return averageTimes;
  }
  public String toString(){
    return new JsonBuilder(generateFactors()).toPrettyString();
  }
  public LazyMap getPreRenderedFactors(){
    //TODO Fix this...
    return (LazyMap) new JsonSlurper().parseText(jsonFactors);
  }

  private String jsonFactors = "{\n" +
          "    \"2228:volte:light\": 88540,\n" +
          "    \"2144:auto:light\": 76210,\n" +
          "    \"3120:volte:light\": 78087,\n" +
          "    \"2575:volte:light\": 75667,\n" +
          "    \"1620:volte:winter\": 93100,\n" +
          "    \"2225:volte:light\": 78295,\n" +
          "    \"2640:auto:heavy\": 81298,\n" +
          "    \"2220:volte:heavy\": 89400,\n" +
          "    \"2340:volte:light\": 77936,\n" +
          "    \"2100:volte:heavy\": 87573,\n" +
          "    \"2690:volte:light\": 80138,\n" +
          "    \"1900:auto:dead\": 77227,\n" +
          "    \"2480:volte:dead\": 78574,\n" +
          "    \"1700:auto:dead\": 80226,\n" +
          "    \"1800:auto:dead\": 79142,\n" +
          "    \"3125:volte:light\": 76904,\n" +
          "    \"2600:auto:heavy\": 80187,\n" +
          "    \"2160:line:light\": 81300,\n" +
          "    \"1680:volte:light\": 84638,\n" +
          "    \"2300:auto:dead\": 81745,\n" +
          "    \"2500:auto:dead\": 78379,\n" +
          "    \"3240:volte:light\": 79541,\n" +
          "    \"2600:auto:dead\": 78682,\n" +
          "    \"2140:line:light\": 76748,\n" +
          "    \"1600:auto:dead\": 76963,\n" +
          "    \"2103:volte:light\": 77500,\n" +
          "    \"2686:volte:light\": 77524,\n" +
          "    \"3232:volte:light\": 75800,\n" +
          "    \"2140:line:heavy\": 81867,\n" +
          "    \"2220:volte:light\": 85592,\n" +
          "    \"1684:volte:light\": 75785,\n" +
          "    \"2000:auto:dead\": 76808,\n" +
          "    \"2100:auto:dead\": 77584,\n" +
          "    \"1686:volte:light\": 88858,\n" +
          "    \"2650:auto:dead\": 75601,\n" +
          "    \"2565:volte:light\": 89200,\n" +
          "    \"1640:line:winter\": 75239,\n" +
          "    \"2100:line:light\": 76020,\n" +
          "    \"2120:volte:winter\": 82450,\n" +
          "    \"2680:volte:light\": 78932,\n" +
          "    \"1660:volte:heavy\": 87407,\n" +
          "    \"1680:volte:heavy\": 90000,\n" +
          "    \"2650:volte:dead\": 78881,\n" +
          "    \"2660:volte:dead\": 79903,\n" +
          "    \"2460:volte:light\": 79430,\n" +
          "    \"2440:volte:light\": 82166,\n" +
          "    \"2700:auto:light\": 75431,\n" +
          "    \"2640:volte:dead\": 79458,\n" +
          "    \"2680:volte:dead\": 80202,\n" +
          "    \"3475:volte:light\": 78900,\n" +
          "    \"2720:auto:light\": 74754,\n" +
          "    \"2120:volte:heavy\": 85302,\n" +
          "    \"2500:auto:light\": 76969,\n" +
          "    \"2670:volte:dead\": 81721,\n" +
          "    \"2570:volte:heavy\": 86800,\n" +
          "    \"3020:volte:light\": 76576,\n" +
          "    \"1640:auto:winter\": 76360,\n" +
          "    \"2550:volte:heavy\": 83300,\n" +
          "    \"2600:volte:dead\": 84800,\n" +
          "    \"2750:auto:light\": 77300,\n" +
          "    \"3000:volte:light\": 76769,\n" +
          "    \"2620:volte:dead\": 92400,\n" +
          "    \"3025:volte:light\": 76617,\n" +
          "    \"2580:volte:light\": 79200,\n" +
          "    \"3575:volte:light\": 74433,\n" +
          "    \"2560:volte:light\": 78900,\n" +
          "    \"2000:volte:heavy\": 78919,\n" +
          "    \"2550:auto:light\": 77596,\n" +
          "    \"2600:volte:winter\": 78400,\n" +
          "    \"2100:auto:winter\": 77923,\n" +
          "    \"2360:volte:light\": 78708,\n" +
          "    \"3440:volte:dead\": 80400,\n" +
          "    \"1710:auto:light\": 74795,\n" +
          "    \"3460:volte:dead\": 80500,\n" +
          "    \"2130:volte:light\": 87400,\n" +
          "    \"2320:volte:light\": 78385,\n" +
          "    \"3450:volte:light\": 77371,\n" +
          "    \"2675:auto:light\": 78200,\n" +
          "    \"3192:volte:dead\": 77362,\n" +
          "    \"2670:volte:light\": 80590,\n" +
          "    \"1750:auto:light\": 75939,\n" +
          "    \"3420:volte:dead\": 80900,\n" +
          "    \"2140:volte:winter\": 81051,\n" +
          "    \"3172:volte:dead\": 77958,\n" +
          "    \"4040:volte:light\": 76995,\n" +
          "    \"3212:volte:dead\": 76600,\n" +
          "    \"3220:volte:light\": 78920,\n" +
          "    \"3152:volte:dead\": 77771,\n" +
          "    \"3212:volte:light\": 87000,\n" +
          "    \"3440:volte:heavy\": 84133,\n" +
          "    \"1194:auto:light\": 79400,\n" +
          "    \"2780:volte:light\": 88433,\n" +
          "    \"2525:auto:light\": 77100,\n" +
          "    \"3000:auto:dead\": 76966,\n" +
          "    \"1640:volte:winter\": 78891,\n" +
          "    \"2370:volte:light\": 77625,\n" +
          "    \"4140:volte:light\": 77652,\n" +
          "    \"3320:volte:light\": 78437,\n" +
          "    \"3200:volte:heavy\": 83900,\n" +
          "    \"2040:volte:winter\": 83158,\n" +
          "    \"4125:volte:light\": 78760,\n" +
          "    \"3152:volte:light\": 78090,\n" +
          "    \"1600:auto:light\": 75706,\n" +
          "    \"1620:auto:light\": 77725,\n" +
          "    \"1640:auto:light\": 74727,\n" +
          "    \"3300:volte:dead\": 79800,\n" +
          "    \"3100:volte:winter\": 80127,\n" +
          "    \"2590:volte:dead\": 77500,\n" +
          "    \"2120:volte:light\": 82271,\n" +
          "    \"1660:auto:light\": 75768,\n" +
          "    \"3320:volte:dead\": 78740,\n" +
          "    \"2760:volte:dead\": 91350,\n" +
          "    \"2550:volte:dead\": 78000,\n" +
          "    \"3100:volte:heavy\": 83200,\n" +
          "    \"4000:volte:light\": 78692,\n" +
          "    \"2600:auto:light\": 78522,\n" +
          "    \"2320:volte:dead\": 78200,\n" +
          "    \"2660:volte:heavy\": 79254,\n" +
          "    \"1680:auto:light\": 76062,\n" +
          "    \"2700:volte:dead\": 87668,\n" +
          "    \"2340:volte:dead\": 81600,\n" +
          "    \"3010:volte:light\": 76135,\n" +
          "    \"2450:auto:light\": 75700,\n" +
          "    \"2900:volte:light\": 77443,\n" +
          "    \"2850:auto:light\": 76977,\n" +
          "    \"2640:auto:light\": 76527,\n" +
          "    \"2720:volte:dead\": 86607,\n" +
          "    \"3150:volte:light\": 76850,\n" +
          "    \"3140:volte:heavy\": 82861,\n" +
          "    \"2740:volte:dead\": 90600,\n" +
          "    \"2020:volte:light\": 78088,\n" +
          "    \"1613:auto:light\": 74100,\n" +
          "    \"2570:volte:light\": 80895,\n" +
          "    \"2660:auto:light\": 77400,\n" +
          "    \"2300:volte:dead\": 80300,\n" +
          "    \"2520:volte:winter\": 87200,\n" +
          "    \"1700:volte:dead\": 87965,\n" +
          "    \"1900:volte:light\": 87000,\n" +
          "    \"3660:volte:light\": 77113,\n" +
          "    \"3165:volte:light\": 79400,\n" +
          "    \"3166:volte:light\": 76587,\n" +
          "    \"2600:line:light\": 81600,\n" +
          "    \"1100:auto:light\": 72916,\n" +
          "    \"2640:volte:heavy\": 79874,\n" +
          "    \"2150:volte:light\": 81500,\n" +
          "    \"3040:volte:light\": 79550,\n" +
          "    \"2080:volte:dead\": 81453,\n" +
          "    \"3420:volte:heavy\": 84600,\n" +
          "    \"2060:volte:winter\": 89647,\n" +
          "    \"2650:volte:light\": 77780,\n" +
          "    \"3200:volte:dead\": 77654,\n" +
          "    \"3272:volte:light\": 74300,\n" +
          "    \"1800:auto:heavy\": 80212,\n" +
          "    \"2144:volte:light\": 80521,\n" +
          "    \"2148:volte:light\": 78712,\n" +
          "    \"1609:auto:dead\": 75835,\n" +
          "    \"2640:line:light\": 76134,\n" +
          "    \"2260:volte:light\": 86640,\n" +
          "    \"2760:volte:light\": 92500,\n" +
          "    \"1646:volte:light\": 77217,\n" +
          "    \"1600:volte:winter\": 84180,\n" +
          "    \"2770:volte:light\": 74100,\n" +
          "    \"1800:auto:light\": 77222,\n" +
          "    \"1900:auto:light\": 76112,\n" +
          "    \"3180:volte:dead\": 77583,\n" +
          "    \"2275:volte:light\": 77050,\n" +
          "    \"3280:volte:dead\": 80387,\n" +
          "    \"1620:volte:dead\": 92133,\n" +
          "    \"1840:volte:dead\": 88050,\n" +
          "    \"1760:volte:light\": 91033,\n" +
          "    \"2540:volte:light\": 80246,\n" +
          "    \"1800:volte:dead\": 94400,\n" +
          "    \"1660:volte:dead\": 83084,\n" +
          "    \"2150:auto:light\": 74393,\n" +
          "    \"1640:volte:heavy\": 87045,\n" +
          "    \"2775:volte:light\": 76785,\n" +
          "    \"2040:volte:heavy\": 88421,\n" +
          "    \"2875:volte:light\": 75800,\n" +
          "    \"2640:volte:light\": 77618,\n" +
          "    \"1860:volte:light\": 83466,\n" +
          "    \"2660:volte:light\": 77946,\n" +
          "    \"1880:volte:light\": 79933,\n" +
          "    \"1800:volte:light\": 79697,\n" +
          "    \"3218:auto:light\": 75800,\n" +
          "    \"2000:volte:dead\": 80077,\n" +
          "    \"2040:volte:dead\": 82556,\n" +
          "    \"2140:volte:heavy\": 83091,\n" +
          "    \"1140:auto:light\": 73000,\n" +
          "    \"2950:auto:light\": 78325,\n" +
          "    \"2140:auto:light\": 76211,\n" +
          "    \"2350:auto:light\": 76237,\n" +
          "    \"2360:auto:light\": 77893,\n" +
          "    \"1600:auto:winter\": 76559,\n" +
          "    \"2100:auto:light\": 76888,\n" +
          "    \"2020:volte:heavy\": 78029,\n" +
          "    \"2200:volte:dead\": 88690,\n" +
          "    \"2040:auto:heavy\": 80900,\n" +
          "    \"2148:auto:dead\": 75836,\n" +
          "    \"2080:auto:light\": 77277,\n" +
          "    \"2600:auto:winter\": 78685,\n" +
          "    \"2550:volte:light\": 77870,\n" +
          "    \"3140:volte:light\": 77270,\n" +
          "    \"2206:volte:light\": 91350,\n" +
          "    \"2040:auto:light\": 77505,\n" +
          "    \"3030:volte:dead\": 79200,\n" +
          "    \"1680:auto:heavy\": 78620,\n" +
          "    \"2640:auto:dead\": 78049,\n" +
          "    \"3250:auto:light\": 77900,\n" +
          "    \"2080:auto:heavy\": 80217,\n" +
          "    \"3186:volte:light\": 76172,\n" +
          "    \"3640:volte:light\": 77635,\n" +
          "    \"2144:volte:dead\": 83154,\n" +
          "    \"1700:volte:winter\": 77700,\n" +
          "    \"2175:volte:light\": 73772,\n" +
          "    \"2800:auto:light\": 77800,\n" +
          "    \"1680:auto:dead\": 77498,\n" +
          "    \"1600:auto:heavy\": 77942,\n" +
          "    \"2609:auto:dead\": 75560,\n" +
          "    \"4140:volte:dead\": 80700,\n" +
          "    \"1640:auto:heavy\": 79267,\n" +
          "    \"1660:volte:light\": 81007,\n" +
          "    \"2675:volte:light\": 76668,\n" +
          "    \"2250:volte:light\": 76376,\n" +
          "    \"2750:volte:light\": 76319,\n" +
          "    \"2240:volte:light\": 82897,\n" +
          "    \"2680:volte:winter\": 81810,\n" +
          "    \"1700:auto:light\": 77890,\n" +
          "    \"3372:volte:light\": 74900,\n" +
          "    \"1664:volte:light\": 75767,\n" +
          "    \"1700:auto:heavy\": 80388,\n" +
          "    \"3140:volte:dead\": 78633,\n" +
          "    \"2350:volte:light\": 75880,\n" +
          "    \"2148:auto:light\": 76110,\n" +
          "    \"1646:volte:dead\": 76091,\n" +
          "    \"2500:volte:winter\": 95400,\n" +
          "    \"1640:auto:dead\": 76334,\n" +
          "    \"2375:volte:light\": 74800,\n" +
          "    \"2140:volte:light\": 79563,\n" +
          "    \"2180:volte:dead\": 85263,\n" +
          "    \"3100:volte:dead\": 79600,\n" +
          "    \"3440:volte:light\": 80700,\n" +
          "    \"2660:volte:winter\": 80221,\n" +
          "    \"4020:volte:light\": 77537,\n" +
          "    \"2100:volte:dead\": 81868,\n" +
          "    \"2140:volte:dead\": 81124,\n" +
          "    \"2450:volte:light\": 74559,\n" +
          "    \"2960:volte:light\": 81930,\n" +
          "    \"2560:volte:dead\": 75700,\n" +
          "    \"3030:volte:light\": 75684,\n" +
          "    \"2000:auto:light\": 75968,\n" +
          "    \"2460:auto:light\": 92800,\n" +
          "    \"2475:volte:light\": 74200,\n" +
          "    \"2000:line:dead\": 75870,\n" +
          "    \"3172:volte:light\": 77752,\n" +
          "    \"2040:volte:light\": 80685,\n" +
          "    \"4120:volte:light\": 78483,\n" +
          "    \"2880:auto:light\": 78375,\n" +
          "    \"2520:volte:dead\": 77200,\n" +
          "    \"2725:volte:light\": 77103,\n" +
          "    \"3000:auto:light\": 75786,\n" +
          "    \"2228:volte:dead\": 89400,\n" +
          "    \"1860:line:light\": 81400,\n" +
          "    \"2144:auto:dead\": 77223,\n" +
          "    \"2600:volte:heavy\": 87900,\n" +
          "    \"3080:volte:light\": 78300,\n" +
          "    \"2188:volte:light\": 81993,\n" +
          "    \"2040:auto:winter\": 80585,\n" +
          "    \"2646:auto:light\": 77267,\n" +
          "    \"2640:volte:winter\": 79598,\n" +
          "    \"1700:volte:heavy\": 81208,\n" +
          "    \"2610:volte:light\": 85400,\n" +
          "    \"2600:volte:light\": 78929,\n" +
          "    \"3140:volte:winter\": 78973,\n" +
          "    \"2720:volte:light\": 87024,\n" +
          "    \"1860:auto:dead\": 82500,\n" +
          "    \"1600:volte:light\": 81231,\n" +
          "    \"1800:auto:winter\": 77718,\n" +
          "    \"1644:auto:dead\": 75307,\n" +
          "    \"2168:volte:dead\": 81598,\n" +
          "    \"2188:volte:dead\": 88366,\n" +
          "    \"1609:volte:light\": 76184,\n" +
          "    \"2500:volte:light\": 78842,\n" +
          "    \"1720:volte:light\": 92790,\n" +
          "    \"1600:volte:heavy\": 77400,\n" +
          "    \"3100:auto:light\": 78805,\n" +
          "    \"2710:volte:light\": 75800,\n" +
          "    \"3140:auto:light\": 76642,\n" +
          "    \"2060:volte:heavy\": 79017,\n" +
          "    \"2850:volte:light\": 75394,\n" +
          "    \"2080:volte:heavy\": 88100,\n" +
          "    \"1680:volte:winter\": 81510,\n" +
          "    \"2148:volte:dead\": 80821,\n" +
          "    \"2540:volte:winter\": 85900,\n" +
          "    \"1700:volte:light\": 84673,\n" +
          "    \"2080:volte:winter\": 82745,\n" +
          "    \"1820:volte:light\": 82764,\n" +
          "    \"2950:volte:light\": 76918,\n" +
          "    \"1840:volte:light\": 82342,\n" +
          "    \"1609:line:light\": 74714,\n" +
          "    \"2970:volte:light\": 77096,\n" +
          "    \"2975:volte:light\": 76100,\n" +
          "    \"1960:volte:light\": 86100,\n" +
          "    \"1940:volte:light\": 86261,\n" +
          "    \"2646:auto:dead\": 79200,\n" +
          "    \"2180:volte:heavy\": 86640,\n" +
          "    \"2000:auto:heavy\": 79208,\n" +
          "    \"3180:volte:light\": 76733,\n" +
          "    \"2050:volte:light\": 76935,\n" +
          "    \"2700:volte:light\": 76676,\n" +
          "    \"2140:line:dead\": 77494,\n" +
          "    \"1640:line:light\": 75275,\n" +
          "    \"3200:auto:light\": 85500,\n" +
          "    \"2050:auto:light\": 75211,\n" +
          "    \"1920:volte:light\": 88800,\n" +
          "    \"2620:volte:heavy\": 91700,\n" +
          "    \"2140:auto:winter\": 78016,\n" +
          "    \"1660:volte:winter\": 82173,\n" +
          "    \"1644:auto:light\": 74786,\n" +
          "    \"2160:volte:winter\": 81378,\n" +
          "    \"3280:auto:light\": 77088,\n" +
          "    \"2168:volte:light\": 79271,\n" +
          "    \"4080:volte:light\": 75857,\n" +
          "    \"2740:volte:light\": 77162,\n" +
          "    \"2625:volte:light\": 77114,\n" +
          "    \"1646:auto:light\": 75576,\n" +
          "    \"2100:auto:heavy\": 83705,\n" +
          "    \"1640:line:dead\": 77410,\n" +
          "    \"2060:volte:light\": 82786,\n" +
          "    \"4180:volte:light\": 76410,\n" +
          "    \"2925:volte:light\": 74806,\n" +
          "    \"3050:volte:light\": 76556,\n" +
          "    \"3420:volte:light\": 81600,\n" +
          "    \"2164:volte:light\": 84413,\n" +
          "    \"2970:volte:dead\": 76480,\n" +
          "    \"3120:volte:winter\": 78742,\n" +
          "    \"2825:volte:light\": 76061,\n" +
          "    \"3180:volte:heavy\": 82776,\n" +
          "    \"2200:auto:light\": 77120,\n" +
          "    \"2990:volte:dead\": 75725,\n" +
          "    \"2180:volte:winter\": 82100,\n" +
          "    \"1604:auto:light\": 87400,\n" +
          "    \"2160:volte:light\": 80607,\n" +
          "    \"2220:auto:light\": 77800,\n" +
          "    \"2100:line:dead\": 77300,\n" +
          "    \"1780:volte:dead\": 98400,\n" +
          "    \"2140:auto:heavy\": 80453,\n" +
          "    \"2940:volte:light\": 81937,\n" +
          "    \"1600:line:light\": 75100,\n" +
          "    \"3192:volte:light\": 79393,\n" +
          "    \"4175:volte:light\": 78100,\n" +
          "    \"2100:volte:winter\": 78217,\n" +
          "    \"2880:volte:light\": 79254,\n" +
          "    \"1760:volte:dead\": 93000,\n" +
          "    \"2800:volte:light\": 76616,\n" +
          "    \"2300:volte:light\": 79647,\n" +
          "    \"2060:volte:dead\": 82324,\n" +
          "    \"2475:auto:light\": 78200,\n" +
          "    \"1720:volte:dead\": 90391,\n" +
          "    \"2146:volte:light\": 95500,\n" +
          "    \"4060:volte:light\": 76658,\n" +
          "    \"1640:volte:light\": 78630,\n" +
          "    \"1609:auto:winter\": 76055,\n" +
          "    \"3200:volte:light\": 77071,\n" +
          "    \"1644:volte:light\": 79887,\n" +
          "    \"2646:volte:light\": 77514,\n" +
          "    \"2380:volte:light\": 78975,\n" +
          "    \"3700:volte:light\": 76171,\n" +
          "    \"1740:volte:light\": 91675,\n" +
          "    \"2520:volte:light\": 79542,\n" +
          "    \"1600:volte:dead\": 90225,\n" +
          "    \"1640:volte:dead\": 80615,\n" +
          "    \"200:auto:light\": 78400,\n" +
          "    \"2160:auto:light\": 80720,\n" +
          "    \"3380:volte:light\": 80700,\n" +
          "    \"1620:volte:heavy\": 87200,\n" +
          "    \"3300:auto:light\": 78296,\n" +
          "    \"1925:auto:light\": 79552,\n" +
          "    \"2525:volte:light\": 75900,\n" +
          "    \"2200:volte:winter\": 79100,\n" +
          "    \"1609:auto:light\": 74082,\n" +
          "    \"3260:volte:light\": 76661,\n" +
          "    \"2990:volte:light\": 76581,\n" +
          "    \"2020:volte:dead\": 78383,\n" +
          "    \"2220:volte:dead\": 88840,\n" +
          "    \"2140:auto:dead\": 77817,\n" +
          "    \"2140:line:winter\": 78810,\n" +
          "    \"2160:volte:heavy\": 84175,\n" +
          "    \"3280:volte:light\": 79192,\n" +
          "    \"2300:auto:light\": 77533,\n" +
          "    \"2420:volte:light\": 83100,\n" +
          "    \"2120:auto:light\": 81450,\n" +
          "    \"2425:volte:light\": 77100,\n" +
          "    \"2060:auto:heavy\": 76900,\n" +
          "    \"1680:volte:dead\": 86568,\n" +
          "    \"2960:auto:light\": 80000,\n" +
          "    \"2400:volte:light\": 76955,\n" +
          "    \"3160:volte:winter\": 79155,\n" +
          "    \"3046:volte:light\": 91600,\n" +
          "    \"2609:auto:light\": 76985,\n" +
          "    \"3160:volte:light\": 77071,\n" +
          "    \"2040:auto:dead\": 78800,\n" +
          "    \"2640:auto:winter\": 78398,\n" +
          "    \"2640:line:dead\": 79260,\n" +
          "    \"3180:volte:winter\": 77900,\n" +
          "    \"3525:volte:light\": 76425,\n" +
          "    \"200:volte:light\": 77400,\n" +
          "    \"3680:volte:light\": 76746,\n" +
          "    \"2200:volte:heavy\": 92409,\n" +
          "    \"2480:auto:light\": 77787,\n" +
          "    \"4150:volte:light\": 76005,\n" +
          "    \"3010:volte:dead\": 75700,\n" +
          "    \"2060:auto:light\": 77213,\n" +
          "    \"1720:auto:light\": 78865,\n" +
          "    \"2204:volte:light\": 76500,\n" +
          "    \"3600:volte:light\": 77400,\n" +
          "    \"3100:volte:light\": 78264,\n" +
          "    \"2325:volte:light\": 78333,\n" +
          "    \"2280:volte:light\": 83300,\n" +
          "    \"3060:volte:light\": 78220,\n" +
          "    \"2208:volte:light\": 80339,\n" +
          "    \"2164:volte:dead\": 83413,\n" +
          "    \"1709:auto:light\": 74700,\n" +
          "    \"1760:auto:light\": 76994,\n" +
          "    \"2480:volte:light\": 77848,\n" +
          "    \"3146:volte:light\": 76913,\n" +
          "    \"2125:volte:light\": 78669,\n" +
          "    \"2413:auto:light\": 76316,\n" +
          "    \"2666:volte:light\": 77549,\n" +
          "    \"2620:volte:light\": 79423,\n" +
          "    \"2000:line:light\": 76195,\n" +
          "    \"1666:volte:light\": 81102,\n" +
          "    \"2080:auto:dead\": 77158,\n" +
          "    \"1620:volte:light\": 84114,\n" +
          "    \"3225:volte:light\": 76800,\n" +
          "    \"2200:volte:light\": 85930,\n" +
          "    \"3300:volte:light\": 77813,\n" +
          "    \"3160:volte:dead\": 78168,\n" +
          "    \"2550:auto:heavy\": 76700,\n" +
          "    \"2300:auto:heavy\": 84600,\n" +
          "    \"2000:volte:light\": 78836,\n" +
          "    \"4160:volte:light\": 77300,\n" +
          "    \"1666:volte:dead\": 91230,\n" +
          "    \"2025:volte:light\": 76441,\n" +
          "    \"2080:volte:light\": 79632,\n" +
          "    \"1820:auto:light\": 84564,\n" +
          "    \"2011:auto:light\": 72099,\n" +
          "    \"2575:auto:light\": 73200,\n" +
          "    \"1609:auto:heavy\": 78120,\n" +
          "    \"2160:volte:dead\": 82049,\n" +
          "    \"3325:volte:light\": 76865,\n" +
          "    \"2184:volte:light\": 83817,\n" +
          "    \"2100:volte:light\": 81229,\n" +
          "    \"1860:auto:light\": 81150,\n" +
          "    \"1650:auto:light\": 72800,\n" +
          "    \"2120:volte:dead\": 88278,\n" +
          "    \"2146:auto:light\": 80044,\n" +
          "    \"3400:volte:light\": 78828,\n" +
          "    \"3160:volte:heavy\": 79337,\n" +
          "    \"2680:volte:heavy\": 78594,\n" +
          "    \"2400:auto:light\": 77457,\n" +
          "    \"2650:auto:light\": 77121,\n" +
          "    \"2440:auto:light\": 81897,\n" +
          "    \"3026:volte:heavy\": 80300,\n" +
          "    \"2180:volte:light\": 83866,\n" +
          "    \"2020:auto:light\": 75966,\n" +
          "    \"3500:volte:light\": 76300,\n" +
          "    \"2590:volte:light\": 78020,\n" +
          "    \"2500:volte:dead\": 79554,\n" +
          "    \"3140:auto:dead\": 77716,\n" +
          "    \"1646:auto:dead\": 77324,\n" +
          "    \"2920:volte:light\": 81032\n" +
          "}";
}
