package ggc;

import java.util.List;

public interface Observer {
  public void update(Notification notification);
  public List<Notification> getNotifications();
  public void wipeNotifications();
}
