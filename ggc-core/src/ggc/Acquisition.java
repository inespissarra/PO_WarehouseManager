package ggc;

import java.io.Serializable;

public class Acquisition extends Transaction {
  private double _payedValue;

  public Acquisition(int key, String partnerKey, String productKey, 
      int units, double payedValue, int paymentDay) {
    super(key, partnerKey, productKey, units, paymentDay);
    _payedValue = payedValue;
  }

  // getters and setters (payedValue)
  public void setPayedValue(double payedValue) {
    _payedValue = payedValue;
  }

  public double getPayedValue() {
    return _payedValue;
  }

  @Override
  public String toString(){
    return "COMPRA|" + super.toString() + "|" + 
      Math.round(getPayedValue()) +  "|" + super.getPaymentDay();
  }

}