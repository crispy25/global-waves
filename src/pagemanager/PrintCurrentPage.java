package pagemanager;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

public final class PrintCurrentPage implements ICommand {
    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        User user = UserManager.searchUser(commandInput.getUsername());
        assert user != null;

        if (user.isOnline()) {
            output.put(Constants.OUTPUT_MESSAGE, user.getCurrentPage().acceptVisit(user));
        } else {
            output.put(Constants.OUTPUT_MESSAGE, commandInput.getUsername() + Constants.IS_OFFLINE);
        }
    }
}
