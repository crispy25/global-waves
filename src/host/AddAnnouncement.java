package host;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import notifications.Notification;
import usermanager.User;
import usermanager.UserManager;

public final class AddAnnouncement implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, String.format(Constants.USER_NON_EXISTENT_MESSAGE,
                    commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.HOST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_HOST.
                    formatted(commandInput.getUsername()));
        } else if (((Host) user).hasAnnouncement(commandInput.getName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.SAME_ANNOUNCEMENT_NAME.
                    formatted(commandInput.getUsername()));
        } else {
            ((Host) user).getAnnouncements().add(
                    new Announcement(commandInput.getName(), commandInput.getDescription()));

            // notify subscribers
            user.getSubscriberManager().notifySubscribers(
                    new Notification(Constants.NEW_ANNOUNCEMENT, Constants.NEW_ANNOUNCEMENT
                            + Constants.FROM_CONTENT_CREATOR.formatted(user.getName())));

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_ADD_ANNOUNCEMENT.
                    formatted(commandInput.getUsername()));
        }
    }
}
