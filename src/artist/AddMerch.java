package artist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import notifications.Notification;
import usermanager.User;
import usermanager.UserManager;

public final class AddMerch implements ICommand {

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

            if (artist.hasMerch(commandInput.getName())) {
                output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername()
                                                        + Constants.SAME_MERCH_NAME);
            } else if (commandInput.getPrice() < 0) {
                output.put(Constants.OUTPUT_MESSAGE, Constants.NEGATIVE_PRICE);
            } else {
                artist.getMerchandise().add(new Merch(commandInput.getName(),
                                                        commandInput.getDescription(),
                                                            commandInput.getPrice()));

                // notify subscribers
                user.getSubscriberManager().notifySubscribers(
                        new Notification(Constants.NEW_MERCH, Constants.NEW_MERCH
                                + Constants.FROM_CONTENT_CREATOR.formatted(user.getName())));

                output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername()
                                                        + Constants.SUCCESSFUL_ADD_MERCH);
            }

        }

    }
}
