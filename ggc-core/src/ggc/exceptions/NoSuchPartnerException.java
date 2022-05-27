package ggc.exceptions;

public class NoSuchPartnerException extends Exception {

  private String _key;

  public NoSuchPartnerException(String key){
    _key = key;
  }

  public String getKey(){ return _key; }
}