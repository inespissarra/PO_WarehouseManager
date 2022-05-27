package ggc;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Partner implements Serializable, Observer{
  private String _key;
  private String _name;
  private String _address;
  private double _acquisitionsValue = 0.0;
  private double _effectiveSaleValue = 0.0;
  private double _saleValuePayed = 0.0;
  private Set<Batch> _batches = new TreeSet<Batch>();
  private Map<Integer, Transaction> _acquisitions = new TreeMap<>();
  private Map<Integer, Breakdown> _breakdowns = new TreeMap<>();
  private Map<Integer, Sale> _sales = new TreeMap<>();
  private List<Notification> _notifications = new LinkedList<>();
  private List<DeliveryType> _notificationsDelivery = new ArrayList<>();

  private Statute _statute = new Normal(this, 0);

  public Partner(String key, String name, String address) {
    _key = key;
    _name = name;
    _address = address;
  }

  public double paySale(int transactionKey, int date){
    Sale sale = getSales().get(transactionKey);
    if(sale!=null && sale.getPaymentDay()==-1){
      double paymentValue = sale.getPaymentValue();
      sale.setPaymentDay(date);
      setSaleValuePayed(getSaleValuePayed() + paymentValue);
      if(sale.getPaymentDeadline() >= date)
        getStatute().addPoints(paymentValue * 10);
      else
        latePayment(date - sale.getPaymentDeadline());
      return paymentValue;
    }
    else
      return 0;
  }

  public void addPoints(double points){
    _statute.addPoints(points);
  }

  protected void setStatute(Statute statute) { _statute = statute; } 

  public void updateState() {
    _statute.update();
  }

  public Statute getStatute() {
    return _statute;
  }

  public void latePayment(int differenceBetweenDays){
    _statute.latePayment(differenceBetweenDays);
  }

  public double calculatePrice(double baseValue, int differenceBetweenDays, String period){
    return getStatute().calculatePrice(baseValue, differenceBetweenDays, period);
  }

  // getters and setters (name)
  public void setName(String name) {
    _name = name;
  }

  public String getName() {
    return _name;
  }

  // getters and setters (id)
  public void setKey(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

  // getters and setters (address)
  public void setAddress(String address) {
    _address = address;
  }

  public String getAddress() {
    return _address;
  }

  // getters and setters (acquisitionsValue)
  public void setAcquisitionsValue(double acquisitionsValue) {
    _acquisitionsValue = acquisitionsValue;
  }

  public double getAcquisitionsValue() {
    return _acquisitionsValue;
  }

  public void addAcquisition(Acquisition acquisition){
    setAcquisitionsValue(getAcquisitionsValue() + acquisition.getPayedValue());
    _acquisitions.put(acquisition.getKey(), acquisition);
  }

  public void addBreakdown(Breakdown breakdown){
    _breakdowns.put(breakdown.getKey(), breakdown);
  }

  public Map<Integer, Breakdown> getBreakdowns() {
    return _breakdowns;
  }

  public Map<Integer, Transaction> getAcquisitions(){
    return _acquisitions;
  }

  // getters and setters (effectiveSaleValue)
  public void setEffectiveSaleValue(double effectiveValue) {
    _effectiveSaleValue = effectiveValue;
  }

  public double getEffectiveSaleValue() {
    return _effectiveSaleValue;
  }

  public void addSale(Sale sale){
    setEffectiveSaleValue(getEffectiveSaleValue() + sale.getBaseValue());
    _sales.put(sale.getKey(), sale);
  }

  public Map<Integer, Sale> getSales(){
    return _sales;
  }

  public Map<Integer, Transaction> getSalesAndBreakdowns() {
    Map<Integer, Transaction> res = new TreeMap<>(_sales);

     
    for (Map.Entry<Integer, Breakdown> breakdown : _breakdowns.entrySet()) {
      res.put(breakdown.getKey(), breakdown.getValue());
    }    

    return res;
  }

  // getters and setters (saleValuePayed)
  public void setSaleValuePayed(double saleValuePayed) {
    _saleValuePayed = saleValuePayed;
  }

  public double getSaleValuePayed() {
    return _saleValuePayed;
  }

  // getters and setters (batches)
  public Set<Batch> getBatches() {
    return _batches;
  }

  public void addBatch(Batch batch) {
    _batches.add(batch);
  }

  public void removeBatch(Batch batch) {
    _batches.remove(batch);
  }

  @Override
  public void update(Notification notification) {
    for(DeliveryType delivery: _notificationsDelivery)
      delivery.deliver(notification);
    _notifications.add(notification);
  }

  @Override
  public List<Notification> getNotifications(){
    return _notifications;
  }

  @Override
  public void wipeNotifications(){
    _notifications = new LinkedList<>();
  }

  @Override
  public String toString() {
    return getKey() + "|" + getName() + "|" + getAddress() + "|" 
        + getStatute() + "|" 
        + Math.round(getAcquisitionsValue()) + "|" 
        + Math.round(getEffectiveSaleValue()) + "|"
        + Math.round(getSaleValuePayed());
  }

}
