package monetization;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class SeeMerch implements ICommand {

    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());

        if (user == null) {
            output.put(Constants.OUTPUT_MESSAGE, Constants.USER_NON_EXISTENT_MESSAGE.
                    formatted(commandInput.getUsername()));
        } else {
            output.putPOJO(Constants.OUTPUT_RESULT, user.getBoughtMerch().clone());
        }
    }
}
