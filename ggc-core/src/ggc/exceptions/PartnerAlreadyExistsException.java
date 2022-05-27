package ggc.exceptions;

public class PartnerAlreadyExistsException extends Exception{
    
  private String _key;
      
  public PartnerAlreadyExistsException(String key){
    _key = key;
  }
      
  public String getKey(){ return _key; }
}
