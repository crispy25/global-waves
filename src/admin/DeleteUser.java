package admin;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class DeleteUser implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                                                    formatted(commandInput.getUsername()));
        } else if (!user.canBeDeleted(commandInput.getTimestamp())) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_CAN_NOT_BE_DELETED.
                                                    formatted(commandInput.getUsername()));
        } else {
            UserManager.getUsers().remove(user);
            user.removeUserData();

            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_DELETED_MESSAGE.
                                                    formatted(commandInput.getUsername()));
        }
    }
}
