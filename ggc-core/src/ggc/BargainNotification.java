package ggc;

public class BargainNotification extends Notification {
  public BargainNotification(String productKey, double price) {
  super(productKey, price);
  }

  @Override
    public String toString() {
      return "BARGAIN|" + super.toString();
    }
}