package ggc;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import java.util.List;
import java.util.LinkedList; // to change efficient later

public class Product implements Serializable, Observable {
  private String _key;
  private double _maxPrice = 0.0;
  private int _totalStock = 0;
  private int _periodDays = 5;
  private Set<Batch> _batches = new TreeSet<Batch>();
  private List<Observer> _observers = new LinkedList<>();
  private Notification _notification = null;

  public Product(String key) {
    _key = key;
  }

  public Product(String key, int periodDays) {
    _key = key;
    _periodDays = periodDays;
  }

  // getters and setters (key)
  public void setKey(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

  // getters and setters (maxPrice)
  public void setMaxPrice(double maxPrice) {
    _maxPrice = maxPrice;
  }

  public double getMaxPrice() {
    return _maxPrice;
  }

  // getters and setters (totalStock)
  public void setTotalStock(int totalStock) {
    _totalStock = totalStock;
  }

  public int getTotalStock() {
    return _totalStock;
  }

  // getters and setters (_periodDays)
  public void setPeriodDays(int periodDays) {
    _periodDays = periodDays;
  }

  public int getPeriodDays() {
    return _periodDays;
  }  

  // getters and setters (_batches)
  public Set<Batch> getBatches() {
    return _batches;
  }

  public Batch getCheaperBatch(){
    Batch b = null;
    double price = 0;
    for(Batch batch : _batches){
      double batchPrice = batch.getPrice();
      if (b==null || batchPrice < price){
        b = batch;
        price = batchPrice;
      }
    }
    return b;
  }
  
  public void addBatch(Batch batch) {
    _batches.add(batch);
  }

  public void removeBatch(Batch batch) {
    _batches.remove(batch);
  }

  public void toggleNotifications(Observer observer){
    if(_observers.contains(observer)){
      removeObserver(observer);
    } else{
      registerObserver(observer);
    }
  }
  
  @Override
  public void registerObserver(Observer observer) {
    _observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    int i = _observers.indexOf(observer);
    if (i >= 0) { _observers.remove(i); }
  }

  @Override
  public void notifyObservers() {
    for (Observer observer: _observers) {
      observer.update(_notification);
    }
  }

  public void notification(Notification notification){
    _notification = notification;
    notifyObservers();
  }

  public Recipe getRecipe(){
    return null;
  }

  public double getAggravation(){
    return 0;
  }

  @Override
  public String toString() {
    return "" + getKey() + "|" + Math.round(getMaxPrice()) 
        + "|" + getTotalStock();
  }

}
