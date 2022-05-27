package ggc;

public class Selection extends Statute {
  public Selection(Partner partner, double score) {
    super(partner, score);
  }

  @Override
  public void promote() {
    _partner.setStatute(new Elite(_partner, _score));
  }

  @Override
  public void demote() {
    _partner.setStatute(new Normal(_partner, _score));
  }

  @Override
  public void update() {
    if (_score > 25000)
      promote();
  }

  @Override
  public void latePayment(int differenceBetweenDays){
    if(differenceBetweenDays>2){
      _score *= 0.1;
      demote();
    }
  }

  @Override
  public double calculatePrice(double baseValue, int diferenceBetweenDays, String period){
    switch(period){
    case "P1":
      return baseValue * 0.9;
    case "P2":
      if(diferenceBetweenDays>=2)
        return baseValue * 0.95;
      else
        return baseValue;
    case "P3":
      if(diferenceBetweenDays>1)
        return baseValue + baseValue * 0.02 * diferenceBetweenDays;
      else
        return baseValue;
    case "P4":
      return baseValue + baseValue * 0.05 * diferenceBetweenDays;
    default:
      return 0;
    }
  }

  @Override
  public String toString() {
    return "SELECTION|" + super.toString();
  }
}