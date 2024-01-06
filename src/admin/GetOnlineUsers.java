package admin;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commandinput.CommandInput;
import constants.Constants;
import contracts.ICommand;
import usermanager.User;
import usermanager.UserManager;

import java.util.ArrayList;

public final class GetOnlineUsers implements ICommand {
    @Override
    public void execute(final CommandInput commandInput, final ObjectNode output) {
        ArrayList<String> result = new ArrayList<>();

        UserManager.getUsersByType(Constants.NORMAL_USER).stream().filter(User::isOnline).
                                                forEach(user -> result.add(user.getName()));

        output.putPOJO(Constants.OUTPUT_RESULT, result);
        output.remove(Constants.OUTPUT_USER);
    }
}
