package ggc;

import java.io.Serializable;

public abstract class Notification implements Serializable{ 
    private String _productKey;
    private double _price;

    public Notification(String productKey, double price) {
      _productKey = productKey;
      _price = price;
    }

    @Override
    public String toString() {
      return _productKey + "|" + Math.round(_price);
    }
  }


  

  

