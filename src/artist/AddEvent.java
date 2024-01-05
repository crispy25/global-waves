package artist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import notifications.Notification;
import usermanager.User;
import usermanager.UserManager;

public final class AddEvent implements ICommand {
    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                    formatted(commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.ARTIST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_AN_ARTIST.
                                                    formatted(commandInput.getUsername()));
        } else {
            Artist artist = (Artist) user;

            if (artist.hasEvent(commandInput.getName())) {
                output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername()
                        + Constants.SAME_EVENT_NAME);
            } else if (!Event.isDateValid(commandInput.getDate())) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.INVALID_DATE.
                        formatted(commandInput.getUsername()));
            } else {
                artist.getEvents().add(new Event(commandInput.getName(),
                        commandInput.getDescription(),
                        commandInput.getDate()));

                // notify subscribers
                user.getSubscriberManager().notifySubscribers(
                        new Notification(Constants.NEW_EVENT, Constants.NEW_EVENT
                                + Constants.FROM_CONTENT_CREATOR.formatted(user.getName())));

                output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername()
                        + Constants.SUCCESSFUL_ADD_EVENT);
            }
        }
    }
}
