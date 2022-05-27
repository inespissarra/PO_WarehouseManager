package ggc;

public class Normal extends Statute { 
  public Normal(Partner partner, double score) { super(partner, score); }

  @Override
  public void promote() {
    if(_score < 25000)
      _partner.setStatute(new Selection(_partner, _score));
    else
      _partner.setStatute(new Elite(_partner, _score));
  }

  @Override
  public void update() {
    if (_score > 2000)
      promote();
  }

  @Override
  public void latePayment(int differenceBetweenDays){
    _score = 0;
  }

  @Override
  public double calculatePrice(double baseValue, int diferenceBetweenDays, String period){
    switch(period){
    case "P1":
      return baseValue * 0.9;
    case "P2":
      return baseValue;
    case "P3":
      return baseValue + baseValue * 0.05 * diferenceBetweenDays;
    case "P4":
      return baseValue + baseValue * 0.1 * diferenceBetweenDays;
    default:
      return 0;
    }
  }

  @Override
  public String toString() {
    return "NORMAL|" + super.toString();
  }
}