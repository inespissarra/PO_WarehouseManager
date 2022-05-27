package ggc.exceptions;

public class NoSuchTransactionException extends Exception {
  private int _key;

  public NoSuchTransactionException(int key){
    _key = key;
  }

  public int getKey(){
    return _key;
  }
}
