package ggc;

public class Elite extends Statute {

  public Elite(Partner partner, double score) {
    super(partner, score);
  }

  @Override
  public void demote() {
    _partner.setStatute(new Selection(_partner, _score));
  }

  public void update() {}

  @Override
  public void latePayment(int differenceBetweenDays){
    if(differenceBetweenDays>15){
      _score *= 0.25;
      demote();
    }
  }

  @Override
  public double calculatePrice(double baseValue, int diferenceBetweenDays, String period){
    switch(period){
    case "P1":
      return baseValue * 0.9;
    case "P2":
      return baseValue * 0.9;
    case "P3":
      return baseValue * 0.95;
    case "P4":
      return baseValue;
    default:
      return 0;
    }
  }

  @Override
  public String toString() {
    return "ELITE|" + super.toString();
  }
}