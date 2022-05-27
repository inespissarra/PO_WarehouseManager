package ggc;

public class Sale extends Transaction {
  private double _baseValue = 0.0;
  private double _paymentValue = 0.0;
  private int _paymentDeadline = 0;

  public Sale(int key, String partnerKey, String productKey, int units, 
      double baseValue, double paymentValue, int paymentDeadline) {
    super(key, partnerKey, productKey, units, -1);
    
    _baseValue = baseValue;
    _paymentValue = paymentValue;
    _paymentDeadline = paymentDeadline;
  }

  // getters and setters (baseValue)
  public void setBaseValue(double baseValue) {
    _baseValue = baseValue;
  }

  public double getBaseValue() {
    return _baseValue;
  }

  // getters and setters (paymentValue)
  public void setPaymentValue(double paymentValue) {
    _paymentValue = paymentValue;
  }

  public double getPaymentValue() {
    return _paymentValue;
  }

  // getters and setters (paymentDeadline)
  public void setPaymentDeadline(int paymentDeadline) {
    _paymentDeadline = paymentDeadline;
  }

  public int getPaymentDeadline() {
    return _paymentDeadline;
  }

  @Override
  public String toString(){
    String sale = "VENDA|" + super.toString() + "|" + Math.round(getBaseValue()) + "|" 
    + Math.round(getPaymentValue()) + "|" + getPaymentDeadline();
    if(super.getPaymentDay()!=-1){
      sale += "|" + super.getPaymentDay();
    }
    return sale;
  }
}