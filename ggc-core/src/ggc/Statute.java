package ggc;

import java.io.Serializable;

public abstract class Statute implements Serializable {
  private Partner _partner;
  private double _score;

  public Statute(Partner partner, double score) { 
    _partner = partner;
    _score = score;
  }

  public void addPoints(double points){
    _score += points;
    update();
  }

  public void promote(){}
  public void demote(){}

  public abstract void update();

  public abstract void latePayment(int differenceBetweenDays);
  public abstract double calculatePrice(double baseValue, int diferenceBetweenDays, String period);

  @Override
  public String toString() {
    return "" + Math.round(_score);
  }
}
