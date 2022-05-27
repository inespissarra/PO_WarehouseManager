package ggc.exceptions;

public class NoSuchProductException extends Exception {

  private String _key;

  public NoSuchProductException(String key){
    _key = key;
  }

  public String getKey(){ return _key; }
}