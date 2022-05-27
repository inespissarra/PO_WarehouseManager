package ggc;

public class NewNotification extends Notification {
  public NewNotification(String productKey, double price) {
    super(productKey, price);
  }

  @Override
  public String toString() {
    return "NEW|" + super.toString();
  }
}