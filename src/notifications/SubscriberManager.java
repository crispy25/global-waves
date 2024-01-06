package notifications;

import contracts.Subscriber;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class SubscriberManager {

   private ArrayList<Subscriber> subscribers = new ArrayList<>();

    /**
     * Check if the subscriber given is present in the list of subscribers
     * @param subscriber the subscriber to search for
     * @return true if it's present, false otherwise
     */
   public boolean hasSubscriber(final Subscriber subscriber) {
        return subscribers.contains(subscriber);
   }

    /**
     * Add the subscriber to the list of subscribers
     * @param subscriber the subscriber to be added
     */
   public void addSubscriber(final Subscriber subscriber) {
       subscribers.add(subscriber);
   }

    /**
     * Remove the subscriber from the list of subscribers
     * @param subscriber the subscriber to be removed
     */
   public void removeSubscriber(final Subscriber subscriber) {
        subscribers.remove(subscriber);
   }

    /**
     * Notify all subscribers with the given notification
     * @param notification the notification to be sent to the subscribers
     * */
   public void notifySubscribers(final Notification notification) {
       for (Subscriber subscriber : subscribers) {
           subscriber.update(notification);
       }
   }
}
