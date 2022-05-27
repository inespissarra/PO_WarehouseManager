package ggc;

import java.io.Serializable;

public class Batch implements Comparable<Batch>, Serializable {
  private String _productKey;
  private String _partnerKey;
  private double _price;
  private int _actualStock;

  public Batch(String productKey, String partnerKey, double price, 
  int actualStock) {
    _productKey = productKey;
    _partnerKey = partnerKey;
    _actualStock = actualStock;
    _price = price;
  }

  // getters and setters (actualStock)
  public void setActualStock(int actualStock) {
    _actualStock = actualStock;
  }

  public int getActualStock() {
    return _actualStock;
  }

  // getters and setters (price)
  public void setPrice(double price) {
    _price = price;
  }

  public double getPrice() {
    return _price;
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

  @Override
  public String toString() {
    return getProductKey() + "|" + getPartnerKey() + "|"
        + Math.round(getPrice()) + "|" + getActualStock();
  }

  @Override
  public int compareTo(Batch other) {
    if (getProductKey().compareToIgnoreCase(other.getProductKey()) == 0) {
      if (getPartnerKey().compareToIgnoreCase(other.getPartnerKey()) == 0) {
        if (getPrice() == other.getPrice()) {
          return getActualStock() - other.getActualStock();
        } else {
          return Double.compare(getPrice(), other.getPrice());
        }
      } else {
        return getPartnerKey().compareToIgnoreCase(other.getPartnerKey());
      }
    }
    return getProductKey().compareToIgnoreCase(other.getProductKey());
  }
}
