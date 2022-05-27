package ggc.exceptions;

public class DateOutOfBoundsException extends Exception {

  private int _days;

  public DateOutOfBoundsException(int days){
    _days = days;
  }

  public int getDays(){ return _days; }
}