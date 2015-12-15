package se.teddy.atg;

import se.teddy.atg.competition.Competition;
import se.teddy.atg.competition.DagensDubbel;
import se.teddy.atg.competition.LunchDubbel;
import se.teddy.atg.mail.Gmail;
import se.teddy.atg.race.RaceDay;
import se.teddy.atg.rest.ATGApi;
import se.teddy.atg.utils.CONDITIONS;
import se.teddy.atg.utils.DATE;
import se.teddy.atg.utils.FILTERS;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.time.DayOfWeek.*;

/**
 * Created by gengdahl on 2015-12-10.
 */
public class Daemon {
  private Competition currentCompetition = null;



  public static void main(String[] args){
    CONDITIONS.MIN_WIN_MARGIN.set(0);
    CONDITIONS.DUBBEL_MIN_WIN_MARGIN.set(0);
    CONDITIONS.DUBBEL_MAX_WIN_ODDS.set(25000);
    CONDITIONS.DUBBEL_MIN_WIN_ODDS.set(1);
    CONDITIONS.BET_FIXED_AMOUNT.set(100);
    CONDITIONS.BET_VARIABLE_FACTOR.set(0);
    CONDITIONS.DEPOSIT_MONTHLY_AMOUNT.set(0);
    CONDITIONS.CACHE_ENABLED.set(Boolean.FALSE);

    FILTERS.ENABLED_COMPETITION_TYPES.add(DagensDubbel.class.getName());
    FILTERS.ENABLED_PRINTOUTS_DAILY_BETS.add(DagensDubbel.class.getName()).add(LunchDubbel.class.getName());
    FILTERS.ENABLED_WEEK_DAYS.add(MONDAY.toString())
            .add(TUESDAY.toString())
            .add(WEDNESDAY.toString())
            .add(THURSDAY.toString())
            .add(FRIDAY.toString())
            .add(SATURDAY.toString())
            .add(SUNDAY.toString());
    Object semaphore = new Object();
    try{
      synchronized(semaphore) {
        semaphore.wait(5 * 60 * 1000); //Wait five minutes until we start to allow time
        //for clock to adjust to correct time and timezone
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }

    new Daemon().daemonLoop();

  }
  public void daemonLoop(){
    String today = DATE.INSTANCE.toString();
    System.out.println("Daemon started " + today);
    scheduleTodaysNotifications(false); //suggestions
    scheduleTodaysNotifications(true);  //Results
    while(true){
      try{
        synchronized(this){
          this.wait(4*60*60*1000); //check if it's a new day every four hours.
          //I doubt there will be a race at 4AM..
        }
        if (!today.equals(DATE.INSTANCE.reset().toString())){
          today = DATE.INSTANCE.toString();
          scheduleTodaysNotifications(false); //suggestions
          scheduleTodaysNotifications(true);  //Results
        }
      }catch(Exception ex){
        ex.printStackTrace();


      }

    }
  }

  /**
   *
   * @param results
   */
  public void scheduleTodaysNotifications(boolean results){
    List<Competition> competitions = new ArrayList<Competition>(0);
    try{
      if (FILTERS.ENABLED_WEEK_DAYS.exists(DATE.INSTANCE.getLocalDate().getDayOfWeek().toString())){
        competitions = new RaceDay(ATGApi.getInstance().getJson("calendar/day/"+DATE.INSTANCE.toString(), null)).listCompetitions();
      }else{
        //TODO, fire off mail with error?
      }
      for (Competition competition : competitions){
        if (competition.getStartTimeMillis() > System.currentTimeMillis()){
          if (results){
            new Timer().schedule(new Notifier(competition),competition.getStartTimeMillis() - System.currentTimeMillis() + 45 * 60 * 1000);
          }else{
            new Timer().schedule(new Notifier(competition),competition.getStartTimeMillis() - System.currentTimeMillis() - 15 * 60 * 1000);
          }
        }else{
          new Timer().schedule(new Notifier(competition),10);
        }
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }

  private class Notifier extends TimerTask{
    private Competition competition;
    public Notifier(Competition competition){
      super();
      this.competition = competition;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
      competition.populate();

      String subject;
      if (competition.getStartTimeMillis() < System.currentTimeMillis()){
        subject = DATE.INSTANCE.toString() + ": results: " + competition.getName().toString().toUpperCase();
      }else{
        subject = DATE.INSTANCE.toString() + ": advice: " + competition.getName().toString().toUpperCase();
      }
      Gmail.sendNotification(subject, competition.getHumanReadableInfo());

    }
  }

}
