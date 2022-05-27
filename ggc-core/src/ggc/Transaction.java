package ggc;

import java.io.Serializable;

public abstract class Transaction implements Serializable {
  private int _key;
  private String _partnerKey;
  private String _productKey;
  private int _units;
  private int _paymentDay;

  public Transaction(int key, String partnerKey, String productKey, int units, int paymentDay) {
    _key = key;
    _partnerKey = partnerKey;
    _productKey = productKey;
    _units = units;
    _paymentDay = paymentDay;
  }

  // getters and setters (key)
  public void setKey(int key) {
    _key = key;
  }

  public int getKey() {
    return _key;
  }

  // getters and setters (partnerKey)
  public void setPartnerKey(String partnerKey) {
    _partnerKey = partnerKey;
  }

  public String getPartnerKey() {
    return _partnerKey;
  }

  // getters and setters (productKey)
  public void setProductKey(String productKey) {
    _productKey = productKey;
  }

  public String getProductKey() {
    return _productKey;
  }

  // getters and setters (units)
  public void setUnits(int units) {
    _units = units;
  }

  public int getUnits() {
    return _units;
  }

  // getters and setters (paymentDay)
  public void setPaymentDay(int paymentDay) {
    _paymentDay = paymentDay;
  }

  public int getPaymentDay() {
    return _paymentDay;
  }

  public String toString(){
    return getKey() + "|" + getPartnerKey() + "|" 
        + getProductKey() + "|" + getUnits();
  }

    
}
