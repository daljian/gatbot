package se.teddy.atg.utils;

import java.util.Random;

/**
 * Created by gengdahl on 2015-12-14.
 */
public enum TUNING {
  DIFF_DIVIDER(2, 2); //2 is found to be good
  private double rangeMin = 0;
  private double rangeMax = 0;
  private double current = 0;
  private Random random = new Random();
  TUNING(double rangeMin, double rangeMax){
    if (rangeMin == rangeMax){
      current = rangeMin;

    }else{
      current = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }
    System.out.println(this +" is using " + current);
  }
  public double doubleValue(){
    return current;
  }

}
