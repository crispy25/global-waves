package host;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class RemoveAnnouncement implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, String.format(Constants.USER_NON_EXISTENT_MESSAGE,
                    commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.HOST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_A_HOST.
                    formatted(commandInput.getUsername()));
        } else if (!((Host) user).hasAnnouncement(commandInput.getName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_ANNOUNCEMENT_WITH_NAME.
                    formatted(commandInput.getUsername()));
        } else {
            ((Host) user).removeAnnouncement(commandInput.getName());

            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_DELETE_ANNOUNCEMENT.
                    formatted(commandInput.getUsername()));
        }
    }
}
