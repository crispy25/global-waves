package admin;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserFactory;
import usermanager.UserManager;

public final class AddUser implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        if (UserManager.hasUser(commandInput.getUsername())) {
            output.put(Constants.OUTPUT_MESSAGE,
                            Constants.USERNAME_TAKEN_ERROR_MESSAGE.
                                    formatted(commandInput.getUsername()));
        } else {
            User newUser = UserFactory.createUser(commandInput);
            UserManager.getUsers().add(newUser);

            output.put(Constants.NORMAL_USER, commandInput.getUsername());
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_ADDED_MESSAGE.
                                                        formatted(commandInput.getUsername()));
        }
    }
}
