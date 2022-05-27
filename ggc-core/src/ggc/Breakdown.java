package ggc;

import java.io.Serializable;

public class Breakdown extends Transaction {
  private double _baseValue;
  private double _paymentValue;
  private String _products;

  public Breakdown(int key, String partnerKey, String productKey,
      int units, double baseValue, double paymentValue, int paymentDay,
      String products) {
    super(key, partnerKey, productKey, units, paymentDay);
    _baseValue = baseValue;
    _paymentValue = paymentValue;
    _products = products;
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

  // getters and setters (products)
  public void setProducts(String products) {
    _products = products;
  }

  public String getProducts() {
    return _products;
  }

  @Override
  public String toString(){
    return "DESAGREGAÇÃO|" + super.toString() + 
      "|" + Math.round(getBaseValue()) + 
      "|" + Math.round(getPaymentValue()) + 
      "|" + super.getPaymentDay() + 
      "|" + getProducts();
  }

}