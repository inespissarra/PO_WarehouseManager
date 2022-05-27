package ggc.exceptions;

public class NotEnoughStockException extends Exception {
  private String _key;
  private int _requestedAmount;
  private int _availableAmount;

  public NotEnoughStockException(String productKey, int requested, int available){
    _key = productKey;
    _requestedAmount = requested;
    _availableAmount = available;
  }

  public String getKey(){
    return _key;
  }

  public int getAmount(){
    return _requestedAmount;
  }

  public int getAvailableAmount(){
    return _availableAmount;
  }
}
