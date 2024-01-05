package contracts;

import notifications.Notification;

public interface Subscriber {

    /**
     * Update the subscriber with the notification received
     * @param notification the notification received
     */
    void update(Notification notification);
}
