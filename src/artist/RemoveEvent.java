package artist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class RemoveEvent implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                                                    formatted(commandInput.getUsername()));
        } else if (!user.getType().equals(Constants.ARTIST_USER)) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NOT_AN_ARTIST.
                                                    formatted(commandInput.getUsername()));
        } else if (!((Artist) user).hasEvent(commandInput.getName())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.NO_EVENT_WITH_NAME.
                                                    formatted(commandInput.getUsername()));
        } else {
            ((Artist) user).removeEvent(commandInput.getName());
            output.put(Constants.OUTPUT_MESSAGE, Constants.SUCCESSFUL_DELETED_EVENT.
                                                    formatted(commandInput.getUsername()));
        }
    }
}
